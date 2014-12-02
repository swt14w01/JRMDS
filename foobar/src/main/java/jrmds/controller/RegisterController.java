package jrmds.controller;

import jrmds.main.JrmdsManagement;
import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {
	@Autowired
	private JrmdsManagement controller;
	@Autowired
	private UserManagement usr;
	
	@RequestMapping(value="/register")
	public String htmlRegister() {
		return "register";
	}
}
