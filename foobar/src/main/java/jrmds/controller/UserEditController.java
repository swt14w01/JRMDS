package jrmds.controller;

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

@Controller
public class UserEditController {
	@Autowired
	private UserManagement usr;
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value="/userProfile", method = { RequestMethod.POST, RequestMethod.GET })
	public String userProfile(WebRequest request, Model model) {
	    String editUser = "";
	    model.addAttribute("editUser", editUser);
		return "userProfile";
	}
	
	/**
	 * Edit the username of the current user.
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

	@RequestMapping(value="/editPassword", method = { RequestMethod.GET })
	public String editPassword(@RequestParam(value="currentPassword")String currentPassword, 
							   @RequestParam(value="newPassword")String newPassword,
							   @RequestParam(value="repeatedNewPassword")String repeatedNewPassword,
							   @CurrentUser RegistredUser currentUser,
							   Model model) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String editUser = "";
		
		if(!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
			editUser = "Wrong password.";
		} else {
			
			if(newPassword.length() < 5) {
				editUser = "Your new password must have 5 or more characters.";
			} else {
				
				if(!newPassword.equals(repeatedNewPassword)) {
					editUser = "Passwords do not match.";
				} else {
					
					RegistredUser userToEdit = usr.getUser(currentUser.getName());
					userToEdit.setPassword(passwordEncoder.encode(newPassword));
					userRepository.save(userToEdit);
				}
			}
		}
		model.addAttribute("editUser", editUser);
		return "userProfile";
	}
	
	@RequestMapping(value="/editEmailAdress", method = { RequestMethod.GET })
	public String editEmailAdress(@RequestParam(value="newEmailAdress")String newEmailAdress,
								  @CurrentUser RegistredUser currentUser) {
		//TODO Test and save new email adress
		return "userProfile";
	}
}
