package test;

import org.junit.Test;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import jrmds.model.RegistredUser;
import jrmds.model.WannabeUser;
import jrmds.user.UserManagement;
import jrmds.user.UserRepository;
import junit.framework.TestCase;

//Schlägt fehl, da es Probleme mit @UsernameExistence und @EmailExistence.
//Wenn beide auskommentiert schlagen nur die entsprechenden Tests fehl.
public class WannbeUserTest extends TestCase {
	
	@Autowired
	private UserManagement usr;
	@Autowired
	private UserRepository userRepository;

    private static Validator validator;

    @BeforeClass
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    WannabeUser testUser = new WannabeUser("TestUser", "password", "password", "email@address.test");

    @Test
    public void testValidWannabeUser() {
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations = validator.validate(testUser);
        assertEquals(0, constraintViolations.size());  
    }
    
    @Test
    public void testUsernameIsEmpty() {
    	
    	testUser.setUsername("");
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations = validator.validate(testUser);
        assertEquals(1, constraintViolations.size());
        assertEquals("Username must not be empty.", constraintViolations.iterator().next().getMessage());
        
        //reset testUser
        testUser.setUsername("TestUser");
    }
    
    @Test
    public void testUsernameAlreadyExists() {
    	
    	RegistredUser savedUser = new RegistredUser(testUser.getUsername(), testUser.getPassword(), testUser.getEmailAdress());
    	usr.createUser(savedUser.getUsername(), savedUser.getPassword(), savedUser.getEmailAdress());
    	
    	//To test whether only the username already exists.
    	testUser.setEmailAdress("email2@adress.test");
    	    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations = validator.validate(testUser);
        assertEquals(1, constraintViolations.size());
        assertEquals("Username already exists.", constraintViolations.iterator().next().getMessage());
        
        //reset UserRepository
        usr.deleteRegistredUser(savedUser);
        //reset testUser
        testUser.setEmailAdress("email@adress.test");
    }
    
    @Test
    public void testPasswordLength() {
    	
    	testUser.setPassword("");
    	testUser.setRepeatedPassword("");
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations1 = validator.validate(testUser);
    	assertEquals(1, constraintViolations1.size());
    	assertEquals("Your password must have 5 or more characters.", constraintViolations1.iterator().next().getMessage());

    	testUser.setPassword("p");
    	testUser.setRepeatedPassword("p");
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations2 = validator.validate(testUser);
    	assertEquals(1, constraintViolations2.size());
    	assertEquals("Your password must have 5 or more characters.", constraintViolations2.iterator().next().getMessage());
    	
    	testUser.setPassword("pa");
    	testUser.setRepeatedPassword("pa");
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations3 = validator.validate(testUser);
    	assertEquals(1, constraintViolations3.size());
    	assertEquals("Your password must have 5 or more characters.", constraintViolations3.iterator().next().getMessage());
    	
    	testUser.setPassword("pas");
    	testUser.setRepeatedPassword("pas");
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations4 = validator.validate(testUser);
    	assertEquals(1, constraintViolations4.size());
    	assertEquals("Your password must have 5 or more characters.", constraintViolations4.iterator().next().getMessage());
    	
    	testUser.setPassword("pass");
    	testUser.setRepeatedPassword("pass");
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations5 = validator.validate(testUser);
    	assertEquals(1, constraintViolations5.size());
    	assertEquals("Your password must have 5 or more characters.", constraintViolations5.iterator().next().getMessage());
    	
    	testUser.setPassword("passw");
    	testUser.setRepeatedPassword("passw");
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations6 = validator.validate(testUser);
    	assertEquals(0, constraintViolations6.size());
    	
    	//reset testUser
    	testUser.setPassword("password");
    	testUser.setRepeatedPassword("password");
    }
    
    @Test
    public void testPasswordsDontMatch(){
    	
    	testUser.setRepeatedPassword("password1");
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations = validator.validate(testUser);
    	assertEquals(1, constraintViolations.size());
    	assertEquals("Passwords do not match.", constraintViolations.iterator().next().getMessage());
    	
    	//reset testUser
    	testUser.setRepeatedPassword("password");
    }
    
    @Test
    public void testInvalidEmailAdresses() {
    	
    	testUser.setEmailAdress("email@adress");
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations1 = validator.validate(testUser);
    	assertEquals(1, constraintViolations1.size());
    	assertEquals("No valid email adress.", constraintViolations1.iterator().next().getMessage());

    	testUser.setEmailAdress("emailAdress");
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations2 = validator.validate(testUser);
    	assertEquals(1, constraintViolations2.size());
    	assertEquals("No valid email adress.", constraintViolations2.iterator().next().getMessage());
    	
    	testUser.setEmailAdress("emailAdress.test");
    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations3 = validator.validate(testUser);
    	assertEquals(1, constraintViolations3.size());
    	assertEquals("No valid email adress.", constraintViolations3.iterator().next().getMessage());
    	
    	//reset testUser
    	testUser.setEmailAdress("email@adress.test");
    }
    
    @Test
    public void testEmaiAdressAlreadyExists() {
    	
    	RegistredUser savedUser = new RegistredUser(testUser.getUsername(), testUser.getPassword(), testUser.getEmailAdress());
    	usr.createUser(savedUser.getUsername(), savedUser.getPassword(), savedUser.getEmailAdress());
    	
    	//To test whether only the email adress already exists.
    	testUser.setUsername("TestUser2");
    	    	
    	Set<ConstraintViolation<WannabeUser>> constraintViolations = validator.validate(testUser);
        assertEquals(1, constraintViolations.size());
        assertEquals("Username already exists.", constraintViolations.iterator().next().getMessage());
        
        //reset UserRepository
        usr.deleteRegistredUser(savedUser);
        //reset testUser
        testUser.setEmailAdress("email@adress.test");
    }
}