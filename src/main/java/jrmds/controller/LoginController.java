package jrmds.controller;

import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Class returns login html document.
 */
@Controller
public class LoginController {
	@Autowired
	private UserManagement userManagement;
	
	@RequestMapping(value="/login")
	public String login() {
		return "login";
	}
}
