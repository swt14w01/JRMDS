package jrmds.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import jrmds.main.JrmdsManagement;
import jrmds.model.Group;
import jrmds.model.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.xml.sax.SAXException;

@Controller
public class XmlLogic {

	
	private XmlValidator _xmlValidator;
	private XmlConverter _converter;
	private JrmdsManagement _jrmdsManagement;
	
	@Autowired
	public XmlLogic(XmlValidator xmlValidator, XmlConverter converter, JrmdsManagement jrmdsManagement)
	{
		_xmlValidator = xmlValidator;
		_converter = converter;
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
			try (InputStream s = url.openStream())
			{
				return validate(new Scanner(s).nextLine());
			}
		}
		catch (IOException ex)
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
	

	public String objectsToXML(Project p, Group g) throws InvalidObjectException, XmlParseException
	{
		Set<jrmds.model.Component> setComponent = _jrmdsManagement.getGroupComponents(p, g);
		if (setComponent == null)
			throw new InvalidObjectException("Die Gruppe existiert nicht oder ist leer");

		return _converter.objectsToXml(setComponent);
	}
	
	public Set<jrmds.model.Component> XmlToObjects(String xmlContent) throws XmlParseException, InvalidObjectException
	{
		String _xmlContent = xmlContent;
		if (_xmlContent == null || _xmlContent == "")
			throw new InvalidObjectException("Die XML ist leer");

		return _converter.XmlToObjects(_xmlContent);
	}
	
	public Set<jrmds.model.Component> XmlToObjects(URL xmlURL) throws XmlParseException, IOException
	{
		String _xmlContent = "";
		URL _xmlUrl = xmlURL;
		try (InputStream s = _xmlUrl.openStream())
		{
			_xmlContent = new Scanner(s).nextLine();
		} 
		if (_xmlContent == null || _xmlContent == "")
			throw new InvalidObjectException("Die XML ist leer");

		return _converter.XmlToObjects(_xmlContent);
	}


	
	public void objectsToJson(){
		
	}

	public Project getProject(String projectName) {
		return _jrmdsManagement.getProject(projectName);
	}

	public Group getGroup(Project project, String groupRefId) {
		return _jrmdsManagement.getGroup(project, groupRefId);
	}

}
