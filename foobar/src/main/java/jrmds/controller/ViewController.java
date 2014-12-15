package jrmds.controller;

import jrmds.model.RegistredUser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import jrmds.security.CurrentUser;

@Controller
public class ViewController extends WebMvcConfigurerAdapter{

	@RequestMapping(value = "/signin", method=RequestMethod.POST)
	public String loginPage() {
		return "login";
	}
	
	@RequestMapping(value = "/test")
	public String currentUserTest(@CurrentUser RegistredUser user) {
	    if(user != null) {
	    	System.out.println(user.getUsername());
	    	return "userProfile";
	    }
	    else {
	    return "start";
	    }
	}
}
