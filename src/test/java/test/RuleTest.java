package test;

import static org.junit.Assert.assertEquals;
import jrmds.model.ComponentType;
import jrmds.model.Concept;
import jrmds.model.Constraint;
import jrmds.model.Rule;

import org.junit.Test;

public class RuleTest {
	Rule ruleconst1 = new Constraint("ruleconstraint1");
	Rule ruleconc1 = new Concept("ruleconcept1");

	
	@Test
	public void CheckConstructor(){ 
		//Constraint Check
		assertEquals("The RefID is wrong!", "ruleconstraint1", ruleconst1.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONSTRAINT, ruleconst1.getType());
		assertEquals("Severity has to be major!", "major", ruleconst1.getSeverity());
		
		//Concept Check
		assertEquals("The RefID is wrong!", "ruleconcept1", ruleconc1.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONCEPT, ruleconc1.getType());
		assertEquals("Severity has to be major!", "major", ruleconc1.getSeverity());
	}
	
	@Test
	public void setSeverityTest(){
		ruleconc1.setSeverity("blocker");
		assertEquals("Severity has to be blocker!", "blocker", ruleconc1.getSeverity());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setFalseSeverityTest(){
		//get-Test not possible
		ruleconc1.setSeverity("sev1");
		ruleconc1.getSeverity();
	}
	
	@Test(expected = NullPointerException.class)
	public void setSeverityNulLTest(){
		ruleconc1.setSeverity(null);
	}
	
	@Test
	public void CheckSecondConstructor(){
		ruleconst1.setSeverity("info");
		ruleconc1.setId(1L);
		ruleconc1.setSeverity("blocker");
		ruleconst1.setId(2L);
		
		Rule ruleconst2 = new Constraint(ruleconst1);
		Rule ruleconc2 = new Concept(ruleconc1);
		
		//Constraint Check
		assertEquals("The RefID is wrong!", "ruleconstraint1", ruleconst2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONSTRAINT, ruleconst2.getType());
		assertEquals("The Severity is wrong!","info", ruleconst2.getSeverity());
		
		//Concept Check
		assertEquals("The RefID is wrong!", "ruleconcept1", ruleconc2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONCEPT, ruleconc2.getType());
		assertEquals("The Severity is wrong!","blocker", ruleconc2.getSeverity());
	}
}
