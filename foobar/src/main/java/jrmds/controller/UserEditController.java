package jrmds.controller;

import jrmds.main.JrmdsManagement;
import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserEditController {
	@Autowired
	private UserManagement usr;
	
	@RequestMapping(value="/editUser")
	public String htmlOutput() {
		return "";
	}
}
