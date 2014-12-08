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
import jrmds.model.Project;
import jrmds.xml.IXmlValidator;
import jrmds.xml.XmlConverter;
import jrmds.xml.XmlLogic;

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
	private IXmlValidator _validator;
	
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
		String url = "url";

		Mockito.when(_validator.validateUrl(url)).thenReturn(true);
		
		boolean result = _testclass.validateUrl(url);
		
		assertTrue(result);
		
		Mockito.verify(_validator).validateUrl(url);
	}
	
	@Test
	public void TestValidateUrlFalse() throws Throwable
	{
		String url = "url";

		Mockito.when(_validator.validateUrl(url)).thenReturn(false);
		
		boolean result = _testclass.validateUrl(url);
		
		assertFalse(result);
		
		Mockito.verify(_validator).validateUrl(url);
	}
	
	@Test
	public void TestValidateUrlExIO() throws SAXException, IOException
	{
		String url = "url";

		Mockito.doThrow(new IOException()).when(_validator).validateUrl(url);
		
		boolean result = _testclass.validateUrl(url);

		assertFalse(result);
		
		Mockito.verify(_validator).validateUrl(url);
}
	
	@Test
	public void TestValidateUrlExSax() throws SAXException, IOException
	{
		String url = "url";

		Mockito.doThrow(new SAXException()).when(_validator).validateUrl(url);

		boolean result = _testclass.validateUrl(url);

		assertFalse(result);

		Mockito.verify(_validator).validateUrl(url);
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
		
	}
	
	@Test
	public void TestGetGroup() throws Throwable
	{
		String projectName = "testName";
		Project p1 = new Project (projectName);
		
		
		Mockito.when(_mgnt.getProject(projectName)).thenReturn(p1);
		
		Project pErg = _testclass.getProject(projectName);
		
		assertEquals(projectName, pErg.getName());
		assertEquals(p1, pErg);
		
	}

}
