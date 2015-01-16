package jrmds.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jrmds.model.RegistredUser;
import jrmds.security.CurrentUser;
import jrmds.user.UserManagement;
import jrmds.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 * To edit attributes of the current user.
 * @author Leroy Buchholz
 *
 */
@Controller
public class UserEditController {
	@Autowired
	private UserManagement usr;
	@Autowired
	UserRepository userRepository;
	
	/**
	 * Sends an empty String to userProfile, to show later error messages.
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/userProfile", method = { RequestMethod.GET })
	public String userProfile(WebRequest request, Model model) {
	    String editUser = "";
	    model.addAttribute("editUser", editUser);
		return "userProfile";
	}
	
	/**
	 * Edit the username of the current user.
	 * Checks if username already exists.
	 * @param newUsername
	 * @param currentUser
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/editUsername", method = { RequestMethod.GET })
	public String editUsername(@RequestParam(value="newUsername")String newUsername,
							   @CurrentUser RegistredUser currentUser,
							   Model model) {
		
		String editUser = "";
		
		if(usr.getUser(newUsername) == null) {
			RegistredUser userToEdit = usr.getUser(currentUser.getName());
			userToEdit.setUsername(newUsername);
			userRepository.save(userToEdit);
			currentUser.setUsername(newUsername);
		}
		else {
			editUser = "Username already exists.";
		}
	    model.addAttribute("editUser", editUser);
	    
		return "userProfile";
	}

	/**
	 * Edit the password of the current user.
	 * Checks the current password, the lenght of the new passwords and weather the new passwords match.
	 * @param currentPassword
	 * @param newPassword
	 * @param repeatedNewPassword
	 * @param currentUser
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/editPassword", method = { RequestMethod.GET })
	public String editPassword(@RequestParam(value="currentPassword")String currentPassword, 
							   @RequestParam(value="newPassword")String newPassword,
							   @RequestParam(value="repeatedNewPassword")String repeatedNewPassword,
							   @CurrentUser RegistredUser currentUser,
							   Model model) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String editUser = "";
		
		//Attention: NOT
		if(!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
			editUser = "Wrong password.";
		} else {
			
			if(newPassword.length() < 5) {
				editUser = "Your new password must have 5 or more characters.";
			} else {
				
				//Attention: NOT
				if(!newPassword.equals(repeatedNewPassword)) {
					editUser = "Passwords do not match.";
				} else {
					
					RegistredUser userToEdit = usr.getUser(currentUser.getName());
					String encodedPassword = passwordEncoder.encode(newPassword);
					userToEdit.setPassword(encodedPassword);
					currentUser.setPassword(encodedPassword);
					userRepository.save(userToEdit);
				}
			}
		}
		model.addAttribute("editUser", editUser);
		return "userProfile";
	}
	
	/**
	 * Edit the email adress of the current user.
	 * Checks if the give is a valid email adress and if the email adress already exists.
	 * @param newEmailAdress
	 * @param currentUser
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/editEmailAdress", method = { RequestMethod.GET })
	public String editEmailAdress(@RequestParam(value="newEmailAdress")String newEmailAdress,
								  @CurrentUser RegistredUser currentUser,
								  Model model) {
		
	    final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	    Matcher matcher = pattern.matcher(newEmailAdress);
	    String editUser = "";
	    
	    //Attention NOT
	    if(!matcher.matches()) {
	    	editUser = "No valid email address.";	
	    } else {
	    	
	    	if(userRepository.findByemailAdress(newEmailAdress) != null) {
	    		editUser = "Email address already exists.";
	    	} else {
	    		
	    		RegistredUser userToEdit = usr.getUser(currentUser.getName());
				userToEdit.setEmailAdress(newEmailAdress);
				userRepository.save(userToEdit);
				currentUser.setEmailAdress(newEmailAdress);
	    	}
	    }
	    model.addAttribute("editUser", editUser);
		return "userProfile";
	}
}
