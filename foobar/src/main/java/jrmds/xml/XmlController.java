package jrmds.xml;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import jrmds.model.Project;

import org.xml.sax.SAXException;


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
	
	public void searchForDuplicates(Project project, Set<String> anotherexternalrepo){
		Set<String> externalrepo = project.getExternalRepos();
	
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
