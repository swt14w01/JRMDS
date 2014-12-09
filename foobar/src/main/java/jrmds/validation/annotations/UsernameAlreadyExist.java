package jrmds.validation.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import jrmds.user.UserManagement;

public class UsernameAlreadyExist implements ConstraintValidator<UsernameExistence, String> { 
	
	@Autowired
	private UserManagement usr;
	
    @Override
    public void initialize(UsernameExistence constraintAnnotation) {       
    }
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context){   
        return (!usernameAlreadyExist(username));
    } 
    public boolean usernameAlreadyExist(String username) {
		if(usr.getUser(username) == null) {
			return false;
		}
		else {
			return true;
		}
    }
}