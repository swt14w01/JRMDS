package jrmds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserEditController {
	
	@RequestMapping(value="/userProfile", method = { RequestMethod.GET, RequestMethod.POST })
	public String userProfile() {
		return "userProfile";
	}
	
	@RequestMapping(value="/editUsername", method = { RequestMethod.GET })
	public String editUsername(@RequestParam(value="newUsername")String newUsername) {
		System.out.println(newUsername);
		return "redirect:/userProfile";
	}
	
	@RequestMapping(value="/editPassword", method = { RequestMethod.GET })
	public String editPassword(@RequestParam(value="currentPassword")String currentPassword, 
							   @RequestParam(value="newPassword")String newPassword,
							   @RequestParam(value="repeatedNewPassword")String repeatedNewPassword) {
		System.out.println(currentPassword);
		return "redirect:/userProfile";
	}
	
	@RequestMapping(value="/editEmailAdress", method = { RequestMethod.GET })
	public String editEmailAdress(@RequestParam(value="newEmailAdress")String newEmailAdress) {
		System.out.println(newEmailAdress);
		return "redirect:/userProfile";
	}
}

