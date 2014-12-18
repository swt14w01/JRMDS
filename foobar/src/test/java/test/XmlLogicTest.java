package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import jrmds.main.JrmdsManagement;
import jrmds.model.Group;
import jrmds.model.Project;
import jrmds.xml.XmlConverter;
import jrmds.xml.XmlLogic;
import jrmds.xml.XmlValidator;

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
		_testclass = new XmlLogic(_validator, _convert, _mgnt);
	}
	
	
	@Test
	public void TestValidateUrlTrue() throws Throwable
	{
		String xmltest = "xmltest";

		Mockito.when(_validator.validate(xmltest)).thenReturn(true);
		
		boolean result = _testclass.validate(xmltest);
		
		assertTrue(result);
		
		Mockito.verify(_validator).validate(xmltest);
	}
	
	@Test
	public void TestValidateUrlFalse() throws Throwable
	{
		String xmltest = "xmltest";

		Mockito.when(_validator.validate(xmltest)).thenReturn(false);
		
		boolean result = _testclass.validate(xmltest);
		
		assertFalse(result);
		
		Mockito.verify(_validator).validate(xmltest);
	}
	
	@Test
	public void TestValidateUrlExIO() throws SAXException, IOException
	{
		String xmltest = "xmltest";

		Mockito.doThrow(new IOException()).when(_validator).validate(xmltest);
		
		boolean result = _testclass.validateUrl(xmltest);

		assertFalse(result);
		
		Mockito.verify(_validator).validate(xmltest);
	}
	
	@Test
	public void TestValidateUrlExSax() throws SAXException, IOException
	{
		String xmltest = "xmltest";

		Mockito.doThrow(new SAXException()).when(_validator).validate(xmltest);

		boolean result = _testclass.validate(xmltest);

		assertFalse(result);

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

		Project p1 = new Project ("prjName");
		
// ...

		Group gErg = _testclass.getGroup(p1, groupName);

		assertEquals(groupName, gErg.getRefID());
		assertEquals( /* ... */null, gErg);
	}

	@Test
	public void TestValidateFile() throws SAXException, IOException 
	{
		String fileURI = "D:\\Studium\\SWT\\Projekt\\jqassistant-rules.xml";
		
		Mockito.when(_validator.validate((String)Mockito.notNull())).thenReturn(true);
		
		_testclass.validateFile(fileURI);

		Mockito.verify(_validator).validate((String)Mockito.notNull());
	
	}
	
}
