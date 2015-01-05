package jrmds.xml;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import net.openhft.lang.io.serialization.CompactBytesMarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jrmds.controller.ErrController;
import jrmds.model.Component;
import jrmds.model.Group;
import jrmds.model.Project;


@Controller
public class XmlController {
		
	private XmlLogic _logic;
	
	
	@Autowired
	public XmlController(XmlLogic logic)
	{
		_logic = logic;
	}

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
		
		PrintStringAsXmlfileToResponse(result, response);
		return null;
	}
	
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
		
		PrintStringAsXmlfileToResponse(result, response);
		return null;
	}


	private void PrintStringAsXmlfileToResponse(String content, HttpServletResponse response) throws IOException
	{
		response.setContentType("text/xml");
		response.setHeader("Content-Disposition", "attachment; filename=test.xml"); 
		ServletOutputStream ostream = response.getOutputStream();
		ostream.println(content);
	}
	
	private ModelAndView GenerateExceptionModel(Exception ex)
	{
		ModelAndView model = new ModelAndView("error2");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		ex.printStackTrace(pw);
		
		String exception = sw.getBuffer().toString();
		
		model.addObject("exception", exception);
		
		return model;	
	}
}
