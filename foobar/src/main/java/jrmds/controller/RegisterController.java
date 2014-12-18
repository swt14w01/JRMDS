package jrmds.controller;

import javax.validation.Valid;

import jrmds.model.WannabeUser;
import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Controller
public class RegisterController {

	@Autowired
	private UserManagement usr;
	
	//At index request an empty WannabeUser-object is bound to the registration.
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String showRegistrationForm(WebRequest request, Model model) {
	    WannabeUser wannabeuser = new WannabeUser();
	    model.addAttribute("wannabeuser", wannabeuser);
	    return "index";
	}
		
	//The filled WannabeUser-object is tested here on invalidity.
	//If it is invalid an Array of Errors is returned and you get Error-messages at the View.
	//But if is is Valid a new RegistredUser-object is saved to the Database and you get redirected to the Login-page.
	@RequestMapping(value = "/", method=RequestMethod.POST)
	public String checkRegistrationForm(@ModelAttribute(value="wannabeuser")@Valid WannabeUser wannabeuser, BindingResult bindingResult) {
	
		if (bindingResult.hasErrors()) {
        return "index";
		}
		else {
			usr.createUser(wannabeuser.getUsername(),getEncryptedPassword(wannabeuser.getPassword()), wannabeuser.getEmailAdress());
			return "redirect:/login";
		}
    
	}

	public String getEncryptedPassword(String password) {  
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  
		String hashedPassword = passwordEncoder.encode(password);  
		return hashedPassword;
	}
	
}
