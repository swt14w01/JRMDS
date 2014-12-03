package jrmds.controller;

import jrmds.main.JrmdsManagement;
import jrmds.model.Group;
import jrmds.user.UserManagement;

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
			@RequestParam(required=false, defaultValue="test") Group grp
			) {
		
		
		return "editGroup";
	}
	
	@RequestMapping(value="/editTemplate")
	public String htmlTemplate() {
		
		
		return "";
	}
}
