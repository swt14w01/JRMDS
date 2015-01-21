package jrmds.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;

/**
 *An annotation to get the registredUser who is actually logged in into a Controller.
 *i.e. Method(@currentUser RegistredUser currentUser)
 *
 * @author Leroy Buchholz
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}