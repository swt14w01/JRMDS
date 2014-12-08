package jrmds.xml;

import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

public interface IXmlValidator {

	boolean validateFile(File localFile) throws SAXException, IOException;

	boolean validateUrl(String urlFile) throws SAXException, IOException;

}
