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
	public void TestGetXmlModelFromJrmdsModel() throws InvalidObjectException
	{
		Group g = XmlTestHelper.CreateGroup(false);
		
		XmlRule result = _testclass.GetXmlModelFromJrmdsModel(g.getReferencedComponents());
		
		assertEquals(1, result.getConcepts().size());
		assertEquals(2, result.getConstraints().size());
	}

	@Test
	public void TestGetXmlFromModelSimple() throws InvalidObjectException, XmlParseException
	{
		Group g = XmlTestHelper.CreateGroup(false);
		
		XmlRule xmlR = _testclass.GetXmlModelFromJrmdsModel(g.getReferencedComponents());
		String xml = _testclass.GetXmlFromModel(xmlR);

		assertNotEquals(0, xml.length());
	}
	

	@Test
	public void TestGetXmlFromModelComplex() throws InvalidObjectException, XmlParseException
	{
		Group g = XmlTestHelper.CreateGroup(true);
		
		XmlRule xmlR = _testclass.GetXmlModelFromJrmdsModel(g.getReferencedComponents());
		String xml = _testclass.GetXmlFromModel(xmlR);

		assertNotEquals(0, xml.length());
	}
	
}
