package jrmds.validation.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for objects which checks whether a password matches to a repeated one.
 * @author Leroy Buchholz
 *
 */
@Target({TYPE,ANNOTATION_TYPE}) 
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordConfirmation.class)
@Documented
public @interface PasswordConfirm { 
    String message() default "Passwords do not match.";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}