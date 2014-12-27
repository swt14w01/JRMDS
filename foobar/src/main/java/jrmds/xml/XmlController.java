package jrmds.xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jrmds.controller.ErrController;
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
			result = _logic.objectsToXML(p, g);
		}
		catch (Exception ex)
		{
			ModelAndView model = new ModelAndView("error2");
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			ex.printStackTrace(pw);
			
			String exception = sw.getBuffer().toString();
			
			model.addObject("exception", exception);
			
			return model;
		}
		
		response.setContentType("text/xml");
		response.setHeader("Content-Disposition", "attachment; filename=test.xml"); 
		ServletOutputStream ostream = response.getOutputStream();
		ostream.println(result);
		return null;
	}


	
}
