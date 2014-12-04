package test;

import static org.junit.Assert.assertEquals;
import jrmds.model.ComponentType;
import jrmds.model.Concept;

import org.junit.Test;

public class ConceptTest {
	Concept conc1 = new Concept("conc1");

	@Test
	public void CheckConstructor(){ 
		assertEquals("The RefID is wrong!", "conc1", conc1.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONCEPT, conc1.getType());
	}
	
	@Test
	public void CheckSecondConstructor(){
		Concept conc2 = new Concept(conc1);
		assertEquals("The RefID is wrong!", "conc1", conc2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONCEPT, conc2.getType());
	}
}
