package jrmds.validation.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for object-attributes which checks whether an email address is valid.
 * @author Leroy Buchholz
 *
 */
@Target({TYPE, FIELD, ANNOTATION_TYPE}) 
@Retention(RUNTIME)
@Constraint(validatedBy = EmailAdressValidation.class)
@Documented
public @interface EmailAdressValidity {   
    String message() default "No valid email adress.";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}