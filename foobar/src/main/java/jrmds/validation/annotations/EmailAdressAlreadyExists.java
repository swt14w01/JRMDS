package jrmds.validation.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import jrmds.user.UserManagement;

/**
 * Logic for the @EmailAdressExistence annotation.
 * @author Leroy Buchholz
 *
 */
public class EmailAdressAlreadyExists implements ConstraintValidator<EmailAdressExistence, String> { 
	
	@Autowired
	private UserManagement usr;
	
    @Override
    public void initialize(EmailAdressExistence constraintAnnotation) {       
    }
    @Override
    public boolean isValid(String emailAdress, ConstraintValidatorContext context){   
        return (emailAdressAlreadyExist(emailAdress));
    } 
	
	public Boolean emailAdressAlreadyExist(String emailAdress) {
		if(usr.getEmailAdress(emailAdress) == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
