package jrmds.validation.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for object-attributes which checks whether an email address already exists in the database.
 * @author Leroy Buchholz
 *
 */
@Target({FIELD,ANNOTATION_TYPE}) 
@Retention(RUNTIME)
@Constraint(validatedBy = EmailAdressAlreadyExists.class)
@Documented
public @interface EmailAdressExistence { 
    String message() default "Email address already exists.";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}
