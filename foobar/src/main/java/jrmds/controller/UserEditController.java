package jrmds.controller;

import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserEditController {
	@Autowired
	private UserManagement usr;
	
	@RequestMapping(value="/userProfile")
	public String userProfile() {
		return "userProfile";
	}
}
