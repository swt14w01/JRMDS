package jrmds.xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jrmds.model.Component;
import jrmds.model.Group;
import jrmds.model.Project;

/**
 * 
 * Controller to communicate with the output, create the XML document
 */
@Controller
public class XmlController {
		
	private XmlLogic _logic;

/**
 * To work with the XmlLogic	
 * @param logic
 */
	@Autowired
	public XmlController(XmlLogic logic)
	{
		_logic = logic;
	}
	
/**
 * Output of a project
 * @param projectName
 * @param response
 * @return
 * @throws IOException
 * @throws XmlParseException
 */
	@RequestMapping(value = "/xml/{project}", method = RequestMethod.GET)
	public ModelAndView objectsToXML(
			@PathVariable("project") String projectName,
			HttpServletResponse response)
					throws IOException, XmlParseException
	{
		String result = "";
		try
		{
			Project p = _logic.getProject(projectName);
			Set<Component> setProject = _logic.getAllProjectComponents(p);
			result = _logic.objectsToXML(setProject);
		}
		catch (Exception ex)
		{
			return GenerateExceptionModel(ex);
		}
		
		PrintStringAsXmlfileToResponse(result, response, projectName);
		return null;
	}
	
/**
 * Output of a group of a project, but return null
 * @param projectName
 * @param groupRefID
 * @param response
 * @return
 * @throws IOException
 * @throws XmlParseException
 */
	@RequestMapping(value = "/xml/{project}/{refId}", method = RequestMethod.GET)
	public ModelAndView objectsToXML(
			@PathVariable("project") String projectName,
			@PathVariable("refId") String groupRefID,
			HttpServletResponse response)
					throws IOException, XmlParseException
	{
		String result = "";
		try
		{
			Project p = _logic.getProject(projectName);
			Group g = _logic.getGroup(p, groupRefID);
			Set<Component> set = _logic.GetComponents(p, g);

			result = _logic.objectsToXML(set);
		}
		catch (Exception ex)
		{
			return GenerateExceptionModel(ex);
		}
		
		PrintStringAsXmlfileToResponse(result, response, projectName + "_" + groupRefID);
		return null;
	}

/**
 * Outputstream print from generated model as a set of components
 * @param content
 * @param response
 * @throws IOException
 */
	 private void PrintStringAsXmlfileToResponse(String content, HttpServletResponse response, String filename) throws IOException
	 {
	  response.setContentType("text/xml");
	  response.setHeader("Content-Disposition", "attachment; name=" + filename + ".xml"); 
	  ServletOutputStream ostream = response.getOutputStream();
	  ostream.println(content);
	 }
	
/**
 * Creates an errorpage with an informative StackTrace
 * @param ex
 * @return
 */
	private ModelAndView GenerateExceptionModel(Exception ex)
	{
		ModelAndView model = new ModelAndView("error2");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		ex.printStackTrace(pw);
		
		String exception = ex.getLocalizedMessage() + "\n\r\n\r" + sw.getBuffer().toString();
		
		model.addObject("exception", exception);
		
		return model;	
	}
}

