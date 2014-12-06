package jrmds.controller;

import javax.validation.Valid;

import jrmds.main.JrmdsManagement;
import jrmds.model.WannabeUser;
import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	private JrmdsManagement controller;
	@Autowired
	private UserManagement usr;
	
	//Beim Seitenaufruf wird ein leeres Objekt vom Typ WannabeUser an den View gebunden
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String showRegistrationForm(WebRequest request, Model model) {
	    WannabeUser wannabeuser = new WannabeUser();
	    model.addAttribute("wannabeuser", wannabeuser);
	    return "index";
	}
		
	//Das gefüllte WannabeUser Objekt wird hier aufgefangen und getestet. Falls keine Fehler auftreten wird ein neuer Registered User in der Datenbank angelegt.
	@RequestMapping(value = "/", method=RequestMethod.POST)
	public String checkRegistrationForm(@ModelAttribute(value="wannabeuser")@Valid WannabeUser wannabeuser, BindingResult bindingResult) {
	
		if (bindingResult.hasErrors()) {
        return "index";
		}
		else {
			usr.createUser(wannabeuser.getUsername(),wannabeuser.getPassword(), wannabeuser.getEmailAdress());
			return "redirect:/login";
		}
    
	}

	/*public String getEncryptedPassword(String password) {  
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  
		String hashedPassword = passwordEncoder.encode(password);  
		return hashedPassword;
	}*/
	
}
