package jrmds.model;

import jrmds.validation.annotations.EmailAdressExistence;
import jrmds.validation.annotations.EmailAdressValidity;
import jrmds.validation.annotations.PasswordConfirm;
import jrmds.validation.annotations.UsernameExistence;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Class for binding an confirming data from registration fields to send them to the Controller.
 * @author Leroy Buchholz
 *
 */
@PasswordConfirm
public class WannabeUser {
	@NotEmpty(message = "Username must not be empty.")
	@UsernameExistence
	private String username;
	@Length(min=5, message= "Your password must have 5 or more characters.")
	private String password;
	private String repeatedPassword;
	@EmailAdressValidity
	@EmailAdressExistence
	private String emailAdress;
	
	public WannabeUser() {}
	
	public WannabeUser(String username, String password, String repeatedPassword, String emailAdress) {
		this.username = username;
		this.password = password;
		this.repeatedPassword = repeatedPassword;
		this.emailAdress = emailAdress;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRepeatedPassword() {
		return repeatedPassword;
	}
	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}
	public String getEmailAdress() {
		return emailAdress;
	}
	public void setEmailAdress(String emailAdress) {
		this.emailAdress = emailAdress;
	}
}

