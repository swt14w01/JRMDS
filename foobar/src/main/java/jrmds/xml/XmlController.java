package jrmds.xml;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
	public void objectsToXML(
			@PathVariable("project") String projectName,
			@PathVariable("refId") String groupRefID,
			HttpServletResponse response)
					throws IOException, XmlParseException
	{
		Project p = _logic.getProject(projectName);
		Group g = _logic.getGroup(p, groupRefID);
		String result = _logic.objectsToXML(p, g);
		
		response.setContentType("text/xml");
		response.setHeader("Content-Disposition", "attachment; filename=test.xml"); 
		ServletOutputStream ostream = response.getOutputStream();
		ostream.println(result);
	}


	
}
