package jrmds.controller;

import jrmds.main.JrmdsManagement;
import jrmds.model.*;
import jrmds.user.UserManagement;

import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComponenController {
	@Autowired
	private JrmdsManagement controller;
	@Autowired
	private UserManagement usr;
	
	@RequestMapping(value="/editRule")
	public String htmlRules() {
		
		return "editRule";
	}
	
	@RequestMapping(value="/editGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String editGroup(
			Model model,
			@RequestParam(required=true, defaultValue="testpro") String project,
			@RequestParam(required=true, defaultValue="fastcheck") String group
			) {
		
		Project p = controller.getProject(project);
		Group g = controller.getGroup(p, group);
		Set<Component> upstream = controller.getReferencingComponents(p, g);
		String taglist = "";
		Iterator<String> iter = g.getTags().iterator();
		while (iter.hasNext()) {
			taglist += iter.next() + ";";
		}
		
		model.addAttribute("project", p);
		model.addAttribute("group", g);
		model.addAttribute("upstream", upstream);
		model.addAttribute("taglist", taglist);
		
		return "editGroup";
	}
	
	@RequestMapping(value="/editTemplate")
	public String htmlTemplate() {
		
		
		return "";
	}
}
