package jrmds.xml;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.stereotype.Controller;
import org.xml.sax.SAXException;

@Controller
public class XmlValidator
{
	
	public boolean validate(String xmlString) throws SAXException, IOException
	{
		URL schemaFile = new URL("https://github.com/buschmais/jqassistant/blob/master/core/analysis/src/main/resources/META-INF/xsd/jqassistant-rules-1.0.xsd");
		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaFile);
		
		Source xmlSource = new StreamSource(new StringReader(xmlString));
		
		Validator validator = schema.newValidator();
		try {
		  validator.validate(xmlSource);
		  return true;
		}
		catch (SAXException e) {
		  System.out.println("Reason: " + e.getLocalizedMessage());
		  return false;
		}
	}
}
