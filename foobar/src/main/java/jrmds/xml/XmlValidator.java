package jrmds.xml;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.util.List;

import org.xml.sax.SAXException;

public class XmlValidator {
	
	public boolean validateFile(File localFile) throws SAXException, IOException{
	URL schemaFile = new URL("https://github.com/buschmais/jqassistant/blob/master/core/analysis/src/main/resources/META-INF/xsd/jqassistant-rules-1.0.xsd");
	Source xmlFile = new StreamSource(localFile);
	SchemaFactory schemaFactory = SchemaFactory
	    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	Schema schema = schemaFactory.newSchema(schemaFile);
	Validator validator = schema.newValidator();
	try {
	  validator.validate(xmlFile);
	  System.out.println(xmlFile.getSystemId() + " is valid");
	  return true;
	} catch (SAXException e) {
	  System.out.println(xmlFile.getSystemId() + " is NOT valid");
	  System.out.println("Reason: " + e.getLocalizedMessage());
	  return false;
	}
	}
	
	public boolean validateUrl(String urlFile) throws SAXException, IOException{
		URL schemaFile = new URL("https://github.com/buschmais/jqassistant/blob/master/core/analysis/src/main/resources/META-INF/xsd/jqassistant-rules-1.0.xsd");
		Source xmlFile = new StreamSource(urlFile);
		SchemaFactory schemaFactory = SchemaFactory
		    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(schemaFile);
		Validator validator = schema.newValidator();
		try {
		  validator.validate(xmlFile);
		  System.out.println(xmlFile.getSystemId() + " is valid");
		  return true;
		} catch (SAXException e) {
		  System.out.println(xmlFile.getSystemId() + " is NOT valid");
		  System.out.println("Reason: " + e.getLocalizedMessage());
		  return false;
		}
		}
	
	
}
