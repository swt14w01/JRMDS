package test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.BeforeClass;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import jrmds.model.WannabeUser;



public class RegisterValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void ValidWannabeUser() {

        WannabeUser wannabeuser = new WannabeUser("Peter", "password", "password", "email@address.com");

      /*   Set<ConstraintViolation<WannabeUser>> constraintViolations =
            validator.validate(wannabeuser);
        assertEquals(0, constraintViolations.size());  */
    }
        /*

  	@Test
    public void carIsValid() {

        Car car = new Car("Morris", "DD-AB-123", 4);

        Set<ConstraintViolation<Car>> constraintViolations =
            validator.validate(car);

        assertEquals(0, constraintViolations.size());
        assertEquals(
                "Case mode must be UPPER.", 
                constraintViolations.iterator().next().getMessage());
        
    }*/
}