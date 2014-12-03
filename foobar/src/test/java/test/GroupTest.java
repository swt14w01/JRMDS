package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jrmds.model.*;

import org.junit.Test;

public class GroupTest {
	Group grp1 = new Group("group1");
	List<String> taglist = new ArrayList<String>();
	
	@Test
	public void CheckConstructor(){ 
		assertEquals("The RefID is wrong!", "group1", grp1.getRefID());
		assertEquals("The Type is wrong!", ComponentType.GROUP, grp1.getType());
		assertEquals("The Tags should be null!", taglist, grp1.getTags());	
		}
	
	@Test
	public void CheckSecondConstructor(){
		taglist.add("Tag1");
		grp1.setTags(taglist);
		Group grp2 = new Group(grp1);
		assertEquals("The RefID is wrong!", "group1", grp2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.GROUP, grp2.getType());
		assertEquals("The Tags are wrong!", taglist, grp1.getTags());
	}
	
	@Test
	public void addReferenceTest(){ //getter OptSeverity() not testable
		Group grp2 = new Group("group2"); grp2.setId(new Long(221));
		Concept conc1 = new Concept("concept1"); conc1.setId(new Long(556));
		Constraint const1 = new Constraint("constraint1"); const1.setId(new Long(512));
		QueryTemplate templ1 = new QueryTemplate("template1"); templ1.setId(new Long(65));
		Map<Integer, String> optseverity = new HashMap<>();
		
		//Group with Severity
		optseverity.put(grp2.getId().intValue(), "severity");
		try {
			grp1.addReference(grp2, "severity");
		} catch (IllegalArgumentException e) {
			assertNotNull("It should not be possible to add a Reference of a Group with a Severity!",e);
		}
		assertNotSame("The Group with the Severity should not have been added!",optseverity, grp1.getOptSeverity());
		optseverity.remove(grp2.getId().intValue(), "severity");
		
		//Template with Severity
		optseverity.put(templ1.getId().intValue(), "severity");
		try {
			grp1.addReference(templ1, "severity");
		} catch (IllegalArgumentException e) {
			assertNotNull("It should not be possible to add a Reference of a Template with a Severity!",e);
		}
		assertNotSame("The Template with the Severity should not have been added!",optseverity, grp1.getOptSeverity());
		optseverity.remove(templ1.getId().intValue(), "severity");
		
		//Concept with Severity
		optseverity.put(conc1.getId().intValue(), "severity");
		try {
			grp1.addReference(conc1, "severity");
		} catch (IllegalArgumentException e) {
			assertNull("It should be possible to add a Reference of a Concept with a Severity!",e);
		}
		assertEquals("The Concept with the Severity should have been added!",optseverity, grp1.getOptSeverity());
	
		
		//Constraint with Severity
		optseverity.put(const1.getId().intValue(), "severity");
		try {
			grp1.addReference(const1, "severity");
		} catch(IllegalArgumentException e) {
			assertNull("It should be possible to add a Reference of a Constraint with a Severity!",e);
		}
		
		assertEquals("The Constraint with the Severity should have been added!",optseverity, grp1.getOptSeverity());
	}
	
	@Test
	public void deleteReferenceTest(){
		Concept conc1 = new Concept("concept1"); conc1.setId(new Long(5));
		Constraint const1 = new Constraint("constraint1"); const1.setId(new Long(6));
		Map<Integer, String> optseverity = new HashMap<>();
		
		//Concept
		optseverity.put(conc1.getId().intValue(), "severity");
		grp1.addReference(conc1, "severity");
		grp1.deleteReference(conc1);
		assertNotSame("The Concept should have been deleted!",optseverity, grp1.getOptSeverity());
		
		//Constraint
		optseverity.remove(conc1.getId().intValue(), "severity");
		optseverity.put(const1.getId().intValue(), "severity");
		grp1.addReference(const1, "severity");
		grp1.deleteReference(const1);
		assertNotSame("The Constraint should have been deleted!",optseverity, grp1.getOptSeverity());	
	}
}
