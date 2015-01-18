package jrmds.validation.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jrmds.model.WannabeUser;

/**
 * Logic for the @PasswordConfirm annotation.
 * @author Leroy Buchholz
 *
 */
public class PasswordConfirmation implements ConstraintValidator<PasswordConfirm, Object> { 
	
    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(PasswordConfirm constraintAnnotation) {       
    }
    
    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){   
        WannabeUser user = (WannabeUser) obj;
        return PasswordsMatch(user.getPassword(),user.getRepeatedPassword()); 
    }
    
    /**
     * Checks whether two passwords match.
     * @param password
     * @param repeatedPassword
     * @return
     */
    public boolean PasswordsMatch(String password, String repeatedPassword) {
    	return password.equals(repeatedPassword);
    }
}