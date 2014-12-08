package jrmds.xml;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import jrmds.main.JrmdsManagement;
import jrmds.model.Group;
import jrmds.model.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.xml.sax.SAXException;

@Controller
public class XmlLogic {

	
	private IXmlValidator _xmlValidator;
	private XmlConverter _converter;
	private JrmdsManagement _jrmdsManagement;
	
	@Autowired
	public XmlLogic(IXmlValidator xmlValidator, XmlConverter converter, JrmdsManagement jrmdsManagement)
	{
		_xmlValidator = xmlValidator;
		_converter = converter;
		_jrmdsManagement = jrmdsManagement;
	}
	

	public boolean validateFile(File localFile)
	{
		return false;
	}
	
	public boolean validateUrl(String urlFile)
	{
		Boolean response = false;	// Standardwert auf False: Wenn Exception kam, darf nicht true zurück gegeben werden!
		try {
			response = _xmlValidator.validateUrl(urlFile);
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
	
	
	
	public void objectsToJson(){
		
	}

	public Project getProject(String projectName) {
		return _jrmdsManagement.getProject(projectName);
	}

	public Group getGroup(Project project, String groupRefId) {
		return _jrmdsManagement.getGroup(project, groupRefId);
	}

}
