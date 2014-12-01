package test;

import static org.junit.Assert.*;
import jrmds.model.*;

import org.junit.Test;

public class ConstraintTest {
	Constraint const1 = new Constraint("const1");
		
	@Test
	public void CheckConstructor(){ 
		assertEquals("The RefID is wrong!", "const1", const1.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONSTRAINT, const1.getType());
	}
	
	@Test
	public void CheckSecondConstructor(){
		Constraint const2 = new Constraint(const1);
		assertEquals("The RefID is wrong!", "const1", const2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONSTRAINT, const2.getType());
	}
}
