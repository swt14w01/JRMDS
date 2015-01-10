package jrmds.xml;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import jrmds.main.JrmdsManagement;
import jrmds.model.Component;
import jrmds.model.Group;
import jrmds.model.Project;

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
	
	/**
	 * Boolean function to validate the file from a string, give it to next function with File parameter
	 * @param filename
	 * @return
	 */
	public boolean validateFile(String filename)
	{
		return validateFile(new File(filename));
	}
	
	/**
	 * validate the File Url, boolean return value
	 * @param file
	 * @return
	 */
	public boolean validateFile(File file)
	{
		try
		{
			return validateUrl(file.toURI().toURL());
		}
		catch (MalformedURLException e)
		{
			return false;
		}
	}

	/**
	 * validate the String Url, boolean return value
	 * @param file
	 * @return
	 */
	public boolean validateUrl(String url)
	{
		try
		{
			return validateUrl(new URL(url));
		}
		catch (IOException ex)
		{
			return false;
		}
	}
	
	/**
	 * validate the Url from external Repositories, boolean return value
	 * @param file
	 * @return
	 */
	public boolean validateUrl(URL url)
	{
		try
		{
			return validate(_extRepo.GetXmlContentFromUrl(url));
		}
		catch (XmlParseException ex)
		{
			return false;
		}
	}
	/**
	 * validate the String with Xml Content
	 * @param xmlString
	 * @return
	 */
	public boolean validate(String xmlString)
	{
		Boolean response = false;	// Standardwert auf False: Wenn Exception kam, darf nicht true zurück gegeben werden!
		try {
			response = _xmlValidator.validate(xmlString);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
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
		{
			for (String extRepoUrl : extRepoUrls)
			{
				String filename = new File(extRepoUrl).getName();
				filename = filename.substring(0, filename.lastIndexOf("."));
				Group extG = new Group("filename");
				
				for (Component extComp : XmlToObjectsFromUrl(extRepoUrl))
				{
					extG.addReference(extComp);
					setProject.add(extComp);
				}
				
				setProject.add(extG);
			}
		}
		return setProject;
	}

}
