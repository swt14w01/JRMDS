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
		assertEquals("Severity has to be null!", null, ruleconst1.getSeverity());
		
		//Concept Check
		assertEquals("The RefID is wrong!", "ruleconcept1", ruleconc1.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONCEPT, ruleconc1.getType());
		assertEquals("Severity has to be null!", null, ruleconc1.getSeverity());
	}
	
	
	public void setSeverityTest(){
		//get-Test not possible
		ruleconc1.setSeverity("sev1");
		ruleconst1.setSeverity("sev2");
		assertEquals("Method setSeverity() is not correct!", "sev1", ruleconc1.getSeverity());
		assertEquals("Method setSeverity() is not correct!", "sev2", ruleconst1.getSeverity());
	}
	
	@Test
	public void CheckSecondConstructor(){
		ruleconst1.setSeverity("sev1");
		ruleconc1.setSeverity("sev2");
		Rule ruleconst2 = new Constraint(ruleconst1);
		Rule ruleconc2 = new Concept(ruleconc1);
		
		//Constraint Check
		assertEquals("The RefID is wrong!", "ruleconstraint1", ruleconst2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONSTRAINT, ruleconst2.getType());
		assertEquals("The Severity is wrong!","sev1", ruleconst2.getSeverity());
		
		//Concept Check
		assertEquals("The RefID is wrong!", "ruleconcept1", ruleconc2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONCEPT, ruleconc2.getType());
		assertEquals("The Severity is wrong!","sev2", ruleconc2.getSeverity());
	}
}
