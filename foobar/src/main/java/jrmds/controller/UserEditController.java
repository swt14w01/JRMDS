package jrmds.controller;

import jrmds.model.RegistredUser;
import jrmds.security.CurrentUser;
import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Controller
public class UserEditController {
	@Autowired
	private UserManagement usr;
	
	@RequestMapping(value="/userProfile", method = { RequestMethod.POST, RequestMethod.GET })
	public String userProfile(WebRequest request, Model model) {
	    String editUser = "";
	    model.addAttribute("editUser", editUser);
		return "userProfile";
	}
	
	@RequestMapping(value="/editUsername", method = { RequestMethod.GET })
	public String editUsername(@RequestParam(value="newUsername")String newUsername,
							   @CurrentUser RegistredUser currentUser,
							   Model model) {
		String editUser = "fuck";
	    model.addAttribute("editUser", editUser);
		//TODO Test and save new username
		return "userProfile";
	}

	@RequestMapping(value="/editPassword", method = { RequestMethod.GET })
	public String editPassword(@RequestParam(value="currentPassword")String currentPassword, 
							   @RequestParam(value="newPassword")String newPassword,
							   @RequestParam(value="repeatedNewPassword")String repeatedNewPassword,
							   @CurrentUser RegistredUser currentUser) {
		//TODO Password-confirmation, saving new password
		return "redirect:/userProfile";
	}
	
	@RequestMapping(value="/editEmailAdress", method = { RequestMethod.GET })
	public String editEmailAdress(@RequestParam(value="newEmailAdress")String newEmailAdress,
								  @CurrentUser RegistredUser currentUser) {
		//TODO Test and save new email adress
		return "redirect:/userProfile";
	}
}
