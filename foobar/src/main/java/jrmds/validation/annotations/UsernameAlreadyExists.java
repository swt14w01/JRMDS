package jrmds.validation.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import jrmds.user.UserManagement;

/**
 * Logic for the @UsernameExistence annotation.
 * @author Leroy Buchholz
 *
 */
public class UsernameAlreadyExists implements ConstraintValidator<UsernameExistence, String> { 
	
	@Autowired
	private UserManagement usr;
	
    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(UsernameExistence constraintAnnotation) {       
    }
    
    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context){   
        return (!usernameAlreadyExists(username));
    } 
    
    /**
     * Checks whether an user with the chosen name already exists in the database.
     * @param username
     * @return whether username already in the database.
     */
    public boolean usernameAlreadyExists(String username) {
		if(usr.getUser(username) == null) {
			return false;
		}
		
		else {
			return true;
		}
    }
}