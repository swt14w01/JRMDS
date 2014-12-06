package jrmds.model;

import jrmds.validation.annotations.PasswordConfirm;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@PasswordConfirm
public class WannabeUser {
	@NotEmpty(message = "Username can not be empty.")
	private String username;
	@NotEmpty(message = "You have to chooses a password.")
	private String password;
	private String repeatedPassword;
	@NotEmpty(message = "You have to specify an email address.")
	@Email(message = "No valid email adress.")
	private String emailAdress;
	
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

