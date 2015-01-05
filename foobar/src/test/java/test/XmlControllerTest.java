package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import jrmds.model.Component;
import jrmds.model.Group;
import jrmds.model.Project;
import jrmds.xml.StringOutputStream;
import jrmds.xml.XmlController;
import jrmds.xml.XmlLogic;
import jrmds.xml.XmlParseException;

@RunWith(MockitoJUnitRunner.class)
public class XmlControllerTest
{

	class TestServletOutputStream
		extends ServletOutputStream
	{

		StringOutputStream _buffer = new StringOutputStream();
		
		@Override
		public void write(int arg) throws IOException {
			// TODO Auto-generated method stub
			_buffer.write(arg);
		}
	
		@Override
		public String toString()
		{
			return _buffer.toString();
		}
	}
	
	
	@Mock
	private XmlLogic _logic;
	
	@Mock
	private HttpServletResponse _response;
	
	private XmlController _testclass; 
	
	
	public XmlControllerTest()
	{
		
	}
	
	
	@Before
	public void InitTest()
	{
		_testclass = new XmlController(_logic);
	}
	
	
	@Test
	public void TestObjectsToXMLProjectGroup() throws IOException, XmlParseException, JAXBException
	{
		String projectName = "pName";
		
		Project p = new Project();
		Set<Component> setComp = new HashSet<Component>();
		setComp.addAll(_logic.GetComponents(p));
		
		
		ServletOutputStream sos = new TestServletOutputStream();
		
		String xmlText = "<xml></xml>";
		
		Mockito.when(_logic.getProject(projectName)).thenReturn(p);
		Mockito.when(_logic.objectsToXML(setComp)).thenReturn(xmlText);

		Mockito.when(_response.getOutputStream()).thenReturn(sos);

		_testclass.objectsToXML(projectName, _response);
		String result = sos.toString();
		
		assertNotEquals(null, result);
		assertNotEquals(0, result.length());
		
	}
	
	@Test
	public void TestObjectsToXMLProject() throws IOException, XmlParseException, JAXBException
	{
		String projectName = "pName";
		String groupId = "gName";
		
		Project p = new Project();
		Group g = XmlTestHelper.CreateGroup(false);
		Set<Component> setComp = new HashSet<Component>();
		setComp.addAll(_logic.GetComponents(p, g));
		
		
		ServletOutputStream sos = new TestServletOutputStream();
		
		String xmlText = "<xml></xml>";
		
		Mockito.when(_logic.getProject(projectName)).thenReturn(p);
		Mockito.when(_logic.getGroup(p, groupId)).thenReturn(g);
		Mockito.when(_logic.objectsToXML(setComp)).thenReturn(xmlText);

		Mockito.when(_response.getOutputStream()).thenReturn(sos);

		_testclass.objectsToXML(projectName, groupId, _response);
		String result = sos.toString();
		
		assertNotEquals(null, result);
		assertNotEquals(0, result.length());
		
	}
}
