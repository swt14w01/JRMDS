package jrmds.controller;

import jrmds.main.JrmdsManagement;
import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	@RequestMapping(value="/editGroup")
	public String htmlGroup() {
		
		
		return "";
	}
	
	@RequestMapping(value="/editTemplate")
	public String htmlTemplate() {
		
		
		return "";
	}
}
