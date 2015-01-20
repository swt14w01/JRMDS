package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import jrmds.main.JrmdsManagement;
import jrmds.model.Group;
import jrmds.model.ImportResult;
import jrmds.model.Project;
import jrmds.xml.ExternalRepoRepository;
import jrmds.xml.XmlConverter;
import jrmds.xml.XmlLogic;
import jrmds.xml.XmlParseException;
import jrmds.xml.XmlValidator;
import jrmds.xml.Model.XmlResultObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.SAXException;

@RunWith(MockitoJUnitRunner.class)
public class XmlLogicTest
{

	@Mock
	private XmlValidator _validator;
	
	@Mock
	private XmlConverter _convert;
	
	@Mock
	private ExternalRepoRepository _extRepo;
	
	@Mock
	private JrmdsManagement _mgnt;
	
	@Mock
	private HttpServletResponse _response;
	
		
	private XmlLogic _testclass; 
	
	
	public XmlLogicTest()
	{
		
	}
	
	
	@Before
	public void InitTest()
	{
		_testclass = new XmlLogic(_validator, _convert, _extRepo, _mgnt);
	}
	
	
	@Test
	public void TestValidateUrlTrue() throws Throwable
	{
		String xmltest = "xmltest";
		XmlResultObject erg = new XmlResultObject(true, "");
		Mockito.when(_validator.validate(xmltest)).thenReturn(erg);
		
		XmlResultObject result = _testclass.validate(xmltest);
		
		assertEquals(erg, result);
		
		Mockito.verify(_validator).validate(xmltest);
	}
	
	@Test
	public void TestValidateUrlFalse() throws Throwable
	{
		String xmltest = "xmltest";
		XmlResultObject erg = new XmlResultObject(false, "");
		Mockito.when(_validator.validate(xmltest)).thenReturn(erg);
		
		XmlResultObject result = _testclass.validate(xmltest);
		
		assertEquals(erg, result);
		
		Mockito.verify(_validator).validate(xmltest);
	}
	
	@Test
	public void TestValidateUrlExIO() throws SAXException, IOException
	{
		String xmltest = "xmltest";
		XmlResultObject erg = new XmlResultObject(false, "");
		Mockito.doThrow(new IOException()).when(_validator).validate(xmltest);
		
		XmlResultObject result = _testclass.validateUrl(xmltest);

		assertEquals(erg, result);
		
		Mockito.verify(_validator).validate(xmltest);
	}
	
	@Test
	public void TestValidateUrlExSax() throws SAXException, IOException
	{
		String xmltest = "xmltest";
		XmlResultObject erg = new XmlResultObject(false, "");
		Mockito.doThrow(new SAXException()).when(_validator).validate(xmltest);

		XmlResultObject result = _testclass.validate(xmltest);

		assertEquals(erg, result);

		Mockito.verify(_validator).validate(xmltest);
}
	
	@Test
	public void TestSearchForDuplicatesFound()
	{
		Project p = new Project();
		p.setExternalRepo(new HashSet<String>(Arrays.asList("rep1","rep2","rep3","rep5")));
		
		Set<String> other = new HashSet<String>(Arrays.asList("rep1","rep3","rep4","rep5"));

		Set<String> result = _testclass.searchForDuplicates(p, other);
		
		assertEquals(3, result.size());
		assertTrue(result.contains("rep1"));
		assertTrue(result.contains("rep3"));
		assertTrue(result.contains("rep5"));
	}

	@Test
	public void TestSearchForDuplicatesEmpty()
	{
		Project p = new Project();
		p.setExternalRepo(new HashSet<String>(Arrays.asList("rep1","rep2","rep3")));
		
		Set<String> other = new HashSet<String>(Arrays.asList("rep4","rep5"));

		Set<String> result = _testclass.searchForDuplicates(p, other);
		
		assertEquals(0, result.size());
	}
	
	@Test
	public void TestGetProject() throws Throwable
	{
		String projectName = "testName";
		Project p1 = new Project (projectName);
		
		Mockito.when(_mgnt.getProject(projectName)).thenReturn(p1);
		
		Project pErg = _testclass.getProject(projectName);
		
		assertEquals(projectName, pErg.getName());
		assertEquals(p1, pErg);

		Mockito.verify(_mgnt).getProject(projectName);
}
	
	@Test
	public void TestGetGroup() throws Throwable
	{
		String groupName = "testgruppe";
		
		Group group = new Group (groupName);

		Project p1 = new Project ("prjName");
		
		Mockito.when(_mgnt.getGroup(p1, groupName)).thenReturn(group);

		Group gErg = _testclass.getGroup(p1, groupName);

		assertEquals(groupName, gErg.getRefID());
		assertEquals( group, gErg);
	}

	@Test
	public void TestValidateFile() throws SAXException, IOException 
	{
		String fileURI = "D:\\Studium\\SWT\\Projekt\\jqassistant-rules.xml";
		
		Mockito.when(_validator.validate((String)Mockito.notNull())).thenReturn(new XmlResultObject(true, ""));
		
		_testclass.validateFile(fileURI);

		Mockito.verify(_validator).validate((String)Mockito.notNull());
	
	}
	
	@Test(expected=InvalidObjectException.class)
	public void TestXmlToObjectsFromStringNull() throws InvalidObjectException, XmlParseException
	{
		//String fileURI = "https://github.com/buschmais/jqassistant/blob/master/examples/rules/naming/jqassistant/model.xml";
		
		String xmlContent = null;
		_testclass.XmlToObjectsFromString(xmlContent);
	}
	
	@Test(expected=InvalidObjectException.class)
	public void TestXmlToObjectsFromStringEmpty() throws InvalidObjectException, XmlParseException
	{
		//String fileURI = "https://github.com/buschmais/jqassistant/blob/master/examples/rules/naming/jqassistant/model.xml";
		
		String xmlContent = "";
		_testclass.XmlToObjectsFromString(xmlContent);
	}
	
	@Test
	public void TestXmlToObjectsFromString() throws XmlParseException, SAXException, IOException
	{
		//String fileURI = "https://github.com/buschmais/jqassistant/blob/master/examples/rules/naming/jqassistant/model.xml";
		String xmlContent = "Test1, Test 2";
		ImportResult setComp = new ImportResult();
		Mockito.when(_validator.validate(xmlContent)).thenReturn(new XmlResultObject(true, ""));
		Mockito.when(_convert.XmlToObjects(xmlContent)).thenReturn(setComp);
		
		ImportResult testContent = _testclass.XmlToObjectsFromString(xmlContent);
		
		assertEquals("The Content is different", testContent, setComp);
	}

	@Test
	public void TestXmlToObjectsFromUrl_String() throws XmlParseException, IOException, SAXException
	{
		// TODO: Dateicontent in temporäre Datei schreiben und als File-URI auslesen, um Unit-Test vom Internet unabhängig zu machen
		String fileURI = "https://raw.githubusercontent.com/buschmais/jqassistant/master/examples/rules/naming/jqassistant/model.xml";
		String xmlContent = "Test1, Test 2";
		ImportResult setComp = new ImportResult();

		Mockito.when(_validator.validate(xmlContent)).thenReturn(new XmlResultObject(true, ""));
		Mockito.when(_extRepo.GetXmlContentFromUrl(fileURI)).thenReturn(xmlContent);
		Mockito.when(_convert.XmlToObjects(xmlContent)).thenReturn(setComp);

		ImportResult result = _testclass.XmlToObjectsFromUrl(fileURI);

		assertEquals("The Content is different", result, setComp);
	}
}
