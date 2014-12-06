package jrmds.controller;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
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
	

	
	public Boolean nullOrEmpty(String fieldContent) {
		if(fieldContent == null || fieldContent.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}

	public Boolean usernameAlreadyExist(String username) {
		if(usr.getUser(username) == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public Boolean emailAdressAlreadyExist(String emailAdress) {
		if(usr.getEmailAdress(emailAdress) == null) {
			return false;
		}
		else {
			return true;
		}
	}
		
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		   }
		catch (AddressException ex) {
			result = false;
		}
		return result;
	}
	

	public Boolean confirmPasswords(String password, String repeatedPassword) {
		if(password == repeatedPassword) {
			return true;
		}
		else {
			return false;
		}
	}
	
	

	/*public String getEncryptedPassword(String password) {  
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  
		String hashedPassword = passwordEncoder.encode(password);  
		return hashedPassword;
	}*/
	
}
