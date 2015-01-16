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
	
    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(EmailAdressExistence constraintAnnotation) {       
    }
    
    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(String emailAdress, ConstraintValidatorContext context){   
        return (!emailAdressAlreadyExist(emailAdress));
    } 
	
	/**
	 * Checks whether an email address is already in the database.
	 * @param emailAdress
	 * @return
	 */
	public Boolean emailAdressAlreadyExist(String emailAdress) {
		if(usr.getEmailAdress(emailAdress) == null) {
			return false;
		}
		
		else {
			return true;
		}
	}
}
