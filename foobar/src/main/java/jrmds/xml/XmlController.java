package jrmds.xml;

<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Set;

import javax.xml.bind.JAXBException;

import jrmds.main.*;
import jrmds.model.Group;
import jrmds.model.Project;
=======
import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import jrmds.model.Component;
import jrmds.model.Group;
import jrmds.model.Project;

import org.xml.sax.SAXException;
>>>>>>> origin/master


@Controller
public class XmlController {
		
	private XmlValidator xmlvalidate = new XmlValidator();
	@Autowired
	private JrmdsManagement jrmdsManage;
	
	public boolean validateFile(File localFile){
		return false;
		
	}
	
	public boolean validateUrl(String urlFile){
		
		Boolean response = true;
		try {
			response = xmlvalidate.validateUrl(urlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	public void searchForDuplicates(Project project, Set<String> anotherexternalrepo){
		Set<String> externalrepo = project.getExternalRepos();
	
		for(String extern : externalrepo){
			for (String anotherextern :anotherexternalrepo){
				if (extern.equals(anotherextern)) {System.out.println("Doppelte URL:"+ anotherextern +"!");}
			}	
		}
	}
	
	public boolean objectstoXML(Project p, Group g) throws InvalidObjectException, JAXBException{
		Set<jrmds.model.Component> setComponent;
		setComponent = jrmdsManage.getGroupComponents(p, g);
		try {
			XmlConverter.objectsToXml(setComponent);
			return true;
		}
		catch (InvalidObjectException e)
		{
			return false;
		}
		catch (JAXBException e)
		{		
			return false;
		}
	}
	
	
	
	public void objectsToJson(){
		
	}
}
