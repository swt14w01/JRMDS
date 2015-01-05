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

@Controller
public class XmlLogic {

	
	private XmlValidator _xmlValidator;
	private XmlConverter _converter;
	private ExternalRepoRepository _extRepo; 
	private JrmdsManagement _jrmdsManagement;
	
	@Autowired
	public XmlLogic(XmlValidator xmlValidator, XmlConverter converter, ExternalRepoRepository extRepo, JrmdsManagement jrmdsManagement)
	{
		_xmlValidator = xmlValidator;
		_converter = converter;
		_extRepo = extRepo;
		_jrmdsManagement = jrmdsManagement;
	}
	

	public boolean validateFile(String filename)
	{
		return validateFile(new File(filename));
	}
	
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
	

	public Set<Component> GetComponents(Project p) throws InvalidObjectException, XmlParseException
	{
		Set<jrmds.model.Component> setComponent = _jrmdsManagement.getProjectComponents(p);
		if (setComponent == null)
			throw new InvalidObjectException("Das Projekt existiert nicht oder ist leer");
		return setComponent;
	}

	public Set<Component> GetComponents(Project p, Group g) throws InvalidObjectException, XmlParseException
	{
		Set<jrmds.model.Component> setComponent = _jrmdsManagement.getGroupComponents(p, g);
		if (setComponent == null)
			throw new InvalidObjectException("Das Projekt oder die Gruppe existiert nicht oder ist leer");
		return setComponent;
	}

	public String objectsToXML(Set<Component> setComponent) throws InvalidObjectException, XmlParseException
	{
		return _converter.objectsToXml(setComponent);
	}
	
	public Set<jrmds.model.Component> XmlToObjectsFromString (String xmlContent) throws XmlParseException, InvalidObjectException
	{
		if (xmlContent == null || xmlContent == "")
			throw new InvalidObjectException("Die XML ist leer");

		return _converter.XmlToObjects(xmlContent);
	}
	
	public Set<jrmds.model.Component> XmlToObjectsFromUrl (String xmlUrl) throws XmlParseException, InvalidObjectException, MalformedURLException
	{
		return XmlToObjectsFromString(_extRepo.GetXmlContentFromUrl(xmlUrl));
	}
	
	public Set<jrmds.model.Component> XmlToObjectsFromUrl (URL xmlUrl) throws XmlParseException, InvalidObjectException
	{
		return XmlToObjectsFromString(_extRepo.GetXmlContentFromUrl(xmlUrl));
	}

	
	public Project getProject(String projectName) {
		return _jrmdsManagement.getProject(projectName);
	}

	public Group getGroup(Project project, String groupRefId) {
		return _jrmdsManagement.getGroup(project, groupRefId);
	}
	
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
