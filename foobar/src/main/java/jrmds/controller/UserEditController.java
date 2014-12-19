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
	
	@RequestMapping(value="/editEmailAdress", method = { RequestMethod.GET })
	public String editEmailAdress(@RequestParam(value="newEmailAdress")String newEmailAdress) {
		System.out.println(newEmailAdress);
		return "redirect:/userProfile";
	}
}

