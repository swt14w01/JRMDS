package jrmds.validation.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jrmds.model.WannabeUser;

public class PasswordConfirmation implements ConstraintValidator<PasswordConfirm, Object> { 
	
    @Override
    public void initialize(PasswordConfirm constraintAnnotation) {       
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){   
        WannabeUser user = (WannabeUser) obj;
        return user.getPassword().equals(user.getRepeatedPassword()); 
    }
}