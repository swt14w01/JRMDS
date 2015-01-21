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

import jrmds.xml.Model.XmlResultObject;

import org.xml.sax.SAXException;

@org.springframework.stereotype.Component
public class XmlValidator
{
	/**
	 * validate against JQAssistant xsd
	 * @param xmlString
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	
	public XmlResultObject validate(String xmlString) throws SAXException, IOException
	{
		URL schemaFile = new URL("https://raw.githubusercontent.com/buschmais/jqassistant/master/core/analysis/src/main/resources/META-INF/xsd/jqassistant-rules-1.0.xsd");
		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaFile);
		
		Source xmlSource = new StreamSource(new StringReader(xmlString));
		
		Validator validator = schema.newValidator();
		try {
		  validator.validate(xmlSource);
		  return new XmlResultObject(true, "OK");
		}
		catch (SAXException e) {
			return new XmlResultObject(false, e.getLocalizedMessage());
		}
	}
}
