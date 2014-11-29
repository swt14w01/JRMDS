package jrmds.controller;

import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
	@Autowired
	private UserManagement userManagement;
	
	@RequestMapping(value="/login")
	public String login() {
		return "login";
	}
}
