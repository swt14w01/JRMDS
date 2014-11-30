package jrmds.controller;

import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserEditController {
	@Autowired
	private UserManagement usr;
	
	@RequestMapping(value="/userProfile")
	public String userProfile() {
		return "userProfile";
	}
}
