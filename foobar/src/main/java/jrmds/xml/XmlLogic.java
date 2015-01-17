package jrmds.xml;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jrmds.main.JrmdsManagement;
import jrmds.model.Component;
import jrmds.model.EnumConflictCause;
import jrmds.model.Group;
import jrmds.model.ImportItem;
import jrmds.model.ImportResult;
import jrmds.model.Project;
import jrmds.xml.Model.XmlResultObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.xml.sax.SAXException;

/**
 * The class XmlLogic provides functions which are needed to get Components, its like a controller
 * 
 *
 */
@Controller
public class XmlLogic {

	
	private XmlValidator _xmlValidator;
	private XmlConverter _converter;
	private ExternalRepoRepository _extRepo; 
	private JrmdsManagement _jrmdsManagement;
	
	/**
	 * Provide the Objects to use the class
	 * @param xmlValidator
	 * @param converter
	 * @param extRepo
	 * @param jrmdsManagement
	 */
	@Autowired
	public XmlLogic(XmlValidator xmlValidator, XmlConverter converter, ExternalRepoRepository extRepo, JrmdsManagement jrmdsManagement)
	{
		_xmlValidator = xmlValidator;
		_converter = converter;
		_extRepo = extRepo;
		_jrmdsManagement = jrmdsManagement;
	}
	
	
	public void validateExternalRepositoryAndThrowException(String url)
	{
		XmlResultObject validationResult = validateUrl(url);
		if (!validationResult.getSuccess())
			throw new IllegalArgumentException("The External Repository is not a valid xml!\n"+  validationResult.getMessage());
	}
	
	/**
	 * Boolean function to validate the file from a string, give it to next function with File parameter
	 * @param filename
	 * @return
	 */
	public XmlResultObject validateFile(String filename)
	{
		return validateFile(new File(filename));
	}
	
	/**
	 * validate the File Url, boolean return value
	 * @param file
	 * @return
	 */
	public XmlResultObject validateFile(File file)
	{
		try
		{
			return validateUrl(file.toURI().toURL());
		}
		catch (MalformedURLException e)
		{
			return new XmlResultObject(false, e.getLocalizedMessage());
		}
	}

	/**
	 * validate the String Url, boolean return value
	 * @param file
	 * @return
	 */
	public XmlResultObject validateUrl(String url)
	{
		try
		{
			return validateUrl(new URL(url));
		}
		catch (IOException ex)
		{
			return new XmlResultObject(false, ex.getLocalizedMessage());
		}
	}
	
	/**
	 * validate the Url from external Repositories, boolean return value
	 * @param file
	 * @return
	 */
	public XmlResultObject validateUrl(URL url)
	{
		try
		{
			return validate(_extRepo.GetXmlContentFromUrl(url));
		}
		catch (XmlParseException ex)
		{
			return new XmlResultObject(false, ex.getLocalizedMessage());
		}
	}
	/**
	 * validate the String with Xml Content
	 * @param xmlString
	 * @return
	 */
	public XmlResultObject validate(String xmlString)
	{
		try {
			return _xmlValidator.validate(xmlString);
		} catch (SAXException e) {
			return new XmlResultObject(false, e.getLocalizedMessage());
		} catch (IOException e) {
			return new XmlResultObject(false, e.getLocalizedMessage());
		}
	}
	
	/**
	 * Compare the Set from external repositories an the project content, and assemble it to one Set without Duplicates
	 * @param project
	 * @param anotherexternalrepo
	 * @return
	 */
	public Set<String> searchForDuplicates(Project project, Set<String> anotherexternalrepo)
	{
		Set<String> result = new HashSet<String>();

		Set<String> externalrepo = project.getExternalRepos();
		if (externalrepo != null)
		{
			for(String extern : externalrepo){
				for (String anotherextern :anotherexternalrepo){
					if (extern.equals(anotherextern) && !result.contains(extern))
						result.add(extern);
				}	
			}
		}

		return result;
	}
	
	/**
	 * Get the Set of Components of a Project by Object project
	 * @param p
	 * @return
	 * @throws InvalidObjectException
	 * @throws XmlParseException
	 */
	public Set<Component> GetComponents(Project p) throws InvalidObjectException, XmlParseException
	{
		Set<jrmds.model.Component> setComponent = _jrmdsManagement.getProjectComponents(p);
		if (setComponent == null)
			throw new InvalidObjectException("Das Projekt existiert nicht oder ist leer");
		return setComponent;
	}

	/**
	 *  Get the Set of Components of a Project by Object project and a contained group
	 * @param p
	 * @param g
	 * @return
	 * @throws InvalidObjectException
	 * @throws XmlParseException
	 */
	public Set<Component> GetComponents(Project p, Group g) throws InvalidObjectException, XmlParseException
	{
		Set<jrmds.model.Component> setComponent = _jrmdsManagement.getGroupComponents(p, g);
		if (setComponent == null)
			throw new InvalidObjectException("Das Projekt oder die Gruppe existiert nicht oder ist leer");
		return setComponent;
	}

	/**
	 * push the request of getting a xml from a set of components into the XmlConverter
	 * @param setComponent
	 * @return
	 * @throws InvalidObjectException
	 * @throws XmlParseException
	 */
	public String objectsToXML(Set<Component> setComponent) throws InvalidObjectException, XmlParseException
	{
		return _converter.objectsToXml(setComponent);
	}
	
	/**
	 * push the request of getting a Object Model from a string with XmlContent into the XmlConverter
	 * @param xmlContent
	 * @return
	 * @throws XmlParseException
	 * @throws InvalidObjectException
	 */
	public Set<jrmds.model.Component> XmlToObjectsFromString (String xmlContent) throws XmlParseException, InvalidObjectException
	{
		if (xmlContent == null || xmlContent == "")
			throw new InvalidObjectException("Die XML ist leer");

		return _converter.XmlToObjects(xmlContent);
	}
	
	/**
	 * push the request of getting a Object Model from a String with Url of a xml into the externalRepoRepository and return a function call of getting Object model from astring with XmlContent
	 * @param xmlUrl
	 * @return
	 * @throws XmlParseException
	 * @throws InvalidObjectException
	 * @throws MalformedURLException
	 */
	public Set<jrmds.model.Component> XmlToObjectsFromUrl (String xmlUrl) throws XmlParseException, InvalidObjectException, MalformedURLException
	{
		return XmlToObjectsFromString(_extRepo.GetXmlContentFromUrl(xmlUrl));
	}
	
	/**
	 * push the request of getting a Object Model from a URL with url of a xml into the externalRepoRepository and return a function call of getting Object model from astring with XmlContent
	 * @param xmlUrl
	 * @return
	 * @throws XmlParseException
	 * @throws InvalidObjectException
	 */
	public Set<jrmds.model.Component> XmlToObjectsFromUrl (URL xmlUrl) throws XmlParseException, InvalidObjectException
	{
		return XmlToObjectsFromString(_extRepo.GetXmlContentFromUrl(xmlUrl));
	}

	/**
	 * push the request to the JrmdsManagement to get a Project by a name
	 * @param projectName
	 * @return
	 */
	public Project getProject(String projectName) {
		return _jrmdsManagement.getProject(projectName);
	}

	/**
	 * push the request to the JrmdsManagement to get a Group by a project an a GroupRefId
	 * @param project
	 * @param groupRefId
	 * @return
	 */
	public Group getGroup(Project project, String groupRefId) {
		return _jrmdsManagement.getGroup(project, groupRefId);
	}
	
	/**
	 * Get a Set of Component with all containing objects from a project by given project
	 * @param project
	 * @return
	 * @throws InvalidObjectException
	 * @throws MalformedURLException
	 * @throws XmlParseException
	 */
	public Set<Component> getAllProjectComponents (Project project) throws InvalidObjectException, MalformedURLException, XmlParseException{
		Set<Component> setProject = GetComponents(project);

		Set<String> extRepoUrls = project.getExternalRepos();
		if (extRepoUrls != null)
			for (String extRepoUrl : extRepoUrls)
				RetrieveExternalRepoContent(extRepoUrl, setProject);

		return setProject;
	}

	public ImportResult analyseXml(Project targetProject, String xmlContent) throws XmlParseException, InvalidObjectException, MalformedURLException
	{
		ImportResult result = new ImportResult();
		Map<String, Set<Component>> extRepoData = new HashMap<String, Set<Component>>();

		// get database stored components
		extRepoData.put("", GetComponents(targetProject));

		// retrieve Set<Component> for all external repos
		Set<String> extRepoUrls = targetProject.getExternalRepos();
		if (extRepoUrls != null)
			for (String extRepoUrl : extRepoUrls)
			{
				Set<Component> components = new HashSet<Component>();
				RetrieveExternalRepoContent(extRepoUrl, components);
				extRepoData.put(extRepoUrl, components);
			}
		
		Set<Component> convertedXml = _converter.XmlToObjects(xmlContent);
		boolean componentOk;
		for (Component xmlComp : convertedXml)
		{
			componentOk = false;
			String id = xmlComp.getRefID();
			for (Map.Entry<String, Set<Component>> entry : extRepoData.entrySet())
			{
				for (Component currComp : entry.getValue())
				{
					if (currComp.getRefID() == id)
					{
						// in db or externalRep
						if(entry.getKey() == "")
							result.AddImportItem(new ImportItem(xmlComp, EnumConflictCause.ExistsInDb, currComp.getType()));
						else
							result.AddImportItem(new ImportItem(xmlComp, EnumConflictCause.ExistsInExternalRep, currComp.getType(), entry.getKey()));
						componentOk = true;
					}
				}
			}

			// if no conflict was found, insert as new
			if (!componentOk)
				result.AddImportItem(new ImportItem(xmlComp));
		}
		
		return result;
	}

	
	private String RetrieveExternalRepoContent(String urlExtRepo, Set<Component> setProject) throws InvalidObjectException, MalformedURLException, XmlParseException
	{
		// compute group for external repository from filename 
		String filename = new File(urlExtRepo).getName();
		int indexOfDot = filename.lastIndexOf(".");
	    if (indexOfDot > 0)
	    	filename = filename.substring(0, indexOfDot);
		Group extG = new Group(filename);
		
		// retrieve xml content and convert to a Set of component 
		for (Component extComp : XmlToObjectsFromUrl(urlExtRepo))
		{
			extG.addReference(extComp);
			setProject.add(extComp);
		}
		setProject.add(extG);
		
		return filename;
	}
	
}
