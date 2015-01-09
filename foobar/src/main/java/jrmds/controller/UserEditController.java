package jrmds.controller;

import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserEditController {
	@Autowired
	private UserManagement usr;
	
	@RequestMapping(value="/userProfile")
	public String userProfile() {
		return "userProfile";
	}
	
	@RequestMapping(value="/editUsername", method = { RequestMethod.GET })
	public String editUsername(@RequestParam(value="newUsername")String newUsername) {
		//TODO Test and save new username
		return "redirect:/userProfile";
	}

	@RequestMapping(value="/editPassword", method = { RequestMethod.GET })
	public String editPassword(@RequestParam(value="currentPassword")String currentPassword, 
							   @RequestParam(value="newPassword")String newPassword,
							   @RequestParam(value="repeatedNewPassword")String repeatedNewPassword) {
		//TODO Password-confirmation, saving new password
		return "redirect:/userProfile";
	}
	
	@RequestMapping(value="/editEmailAdress", method = { RequestMethod.GET })
	public String editEmailAdress(@RequestParam(value="newEmailAdress")String newEmailAdress) {
		//TODO Test and save new email adress
		return "redirect:/userProfile";
	}
}
