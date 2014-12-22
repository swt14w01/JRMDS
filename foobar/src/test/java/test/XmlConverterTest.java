package test;

import static org.junit.Assert.*;

import java.io.InvalidObjectException;

import jrmds.model.Group;
import jrmds.xml.XmlConverter;
import jrmds.xml.XmlParseException;
import jrmds.xml.Model.XmlRule;

import org.junit.Before;
import org.junit.Test;

public class XmlConverterTest {

	XmlConverter _testclass;
	
	@Before
	public void Init()
	{
		_testclass = new XmlConverter();
	}

	@Test
	public void TestGetXmlFromModelSimple() throws InvalidObjectException, XmlParseException
	{
		Group g = XmlTestHelper.CreateGroup(false);
		
		String xml = _testclass.objectsToXml(g.getReferencedComponents());

		assertNotEquals(0, xml.length());
	}
	

	@Test
	public void TestGetXmlFromModelComplex() throws InvalidObjectException, XmlParseException
	{
		Group g = XmlTestHelper.CreateGroup(true);
		
		String xml = _testclass.objectsToXml(g.getReferencedComponents());

		assertNotEquals(0, xml.length());
	}
	
}
