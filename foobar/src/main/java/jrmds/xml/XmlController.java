package jrmds.xml;

import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import jrmds.main.*;
import jrmds.model.Project;


public class XmlController {
	
	private XmlValidator xmlvalidate = new XmlValidator();
	
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
	
	public void searchForDuplicates(Project project, List<String> anotherexternalrepo){
		List<String> externalrepo = project.getExternalRepos();
	
		for(String extern : externalrepo){
			for (String anotherextern :anotherexternalrepo){
				if (extern.equals(anotherextern)) {System.out.println("Doppelte URL:"+ anotherextern +"!");}
			}	
		}
	}
	
	public void objectsToXml(){
		
	}
	
	public void objectsToJson(){
		
	}
}
