package jrmds.validation.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target({FIELD,ANNOTATION_TYPE}) 
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameAlreadyExist.class)
@Documented
public @interface UsernameExistence { 
    String message() default "Username already exists.";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}