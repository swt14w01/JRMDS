package jrmds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserEditController {
	
	@RequestMapping(value="/userProfile")
	public String userProfile() {
		return "userProfile";
	}
}
