package jrmds.validation.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for object-attributes which checks whether a username already exists in the database.
 * @author Leroy Buchholz
 *
 */
@Target({FIELD,ANNOTATION_TYPE}) 
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameAlreadyExists.class)
@Documented
public @interface UsernameExistence { 
    String message() default "Username already exists.";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}