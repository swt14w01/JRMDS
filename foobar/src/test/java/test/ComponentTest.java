package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jrmds.model.Component;
import jrmds.model.ComponentType;
import jrmds.model.Concept;
import jrmds.model.Constraint;
import jrmds.model.Group;
import jrmds.model.QueryTemplate;

import org.junit.Test;
import org.springframework.dao.DuplicateKeyException;

public class ComponentTest {
	Component conc1 = new Concept("concept1");
	Component const1 = new Constraint("constraint1");
	Component templ1 = new QueryTemplate("template1");
	Component grp1 = new Group("group1");
	
	@Test
	public void setIdTest(){
		//Works for all Components equally
		conc1.setId(1L);
		assertEquals("Method setId() doesn't work correctly!", (Long) 1L, conc1.getId());
	}
	
	@Test(expected = NullPointerException.class)
	public void setIdNullTest(){
		//Works for all Components equally
		conc1.setId(null);
	}
	
	@Test
	public void getRefIDTest(){
		//Works for all Components equally
		assertEquals("Method getRefID() doesn't return right ID!", "concept1",conc1.getRefID());
	}
	
	@Test
	public void setRefIDTest(){
		//Works for all Components equally
		conc1.setRefID("newconcept1");
		assertEquals("Method setRefID() doesn't set the refID correctly!", "newconcept1",conc1.getRefID());
	}
	
	@Test(expected = NullPointerException.class)
	public void setRefIDNullTest(){
		//Works for all Components equally
		conc1.setRefID(null);
	}
	
	@Test
	public void getTypeTest(){
		//Works for all Components equally
		assertEquals("Method getType() doesn't return the right type!", ComponentType.CONCEPT, conc1.getType());
	}
	
	@Test
	public void setTypeTest(){
		//Works for all Components equally
		conc1.setType(ComponentType.CONSTRAINT);
		assertEquals("Method setType() doesn't set the type correctly!", ComponentType.CONSTRAINT, conc1.getType());
	}
	
	@Test(expected = NullPointerException.class)
	public void setTypeNullTest(){
		//Works for all Components equally
		conc1.setType(null);
	}
	
	@Test
	public void getTagsTest(){
		//Works for all Components equally
		List<String> taglist = new ArrayList<String>();
		assertEquals("Method getTags() doesn't return the right list!",taglist,conc1.getTags());	
	}
	
	@Test
	public void addTagTest(){
		//Works for all Components equally
		List<String> taglist = new ArrayList<String>();
		taglist.add("Tag1");
		conc1.addTag("Tag1");
		assertEquals("Method addTag() doesn't add the Tag correctly!",taglist,conc1.getTags());
		
	}
	
	@Test(expected = NullPointerException.class)
	public void addTagNullTest(){
		//Works for all Components equally
		conc1.addTag(null);
	}
	
	@Test
	public void setTagsTest(){
		//Works for all Components equally
		List<String> taglist = new ArrayList<String>();
		taglist.add("Tag1");
		taglist.add("Tag2");
		conc1.setTags(taglist);
		assertEquals("Method setTags() doesn't add the List of Tags correctly!",taglist,conc1.getTags());
	}
	
	@Test(expected = NullPointerException.class)
	public void setTagsNullTest(){
		//Works for all Components equally
		conc1.setTags(null);
	}
	
	@Test
	public void deleteTagTest(){
		//Works for all Components equally
		List<String> taglist = new ArrayList<String>();
		taglist.add("Tag1");
		taglist.add("Tag2");
		conc1.setTags(taglist);
		conc1.deleteTag("Tag2");
		taglist.remove("Tag2");
		assertEquals("Method deleteTag() doesn't delete Tag correctly!",taglist,conc1.getTags());
	
	}
	
	@Test(expected = NullPointerException.class)
	public void deleteTagsNullTest(){
		//Works for all Components equally
		conc1.deleteTag(null);
	}

	@Test
	public void addReferenceTest(){
		//Concepts
		try {
			conc1.addReference(conc1);
		} catch(IllegalArgumentException e) {
			assertNull("Concepts can have Concepts as Reference!", e);
		}
		try {
			conc1.addReference(const1);
		} catch(IllegalArgumentException e) {
			assertNotNull("Concepts can not have Constraints as Reference!", e);
		}
		try {
			conc1.addReference(grp1);
		} catch(IllegalArgumentException e) {
			assertNotNull("Concepts can not have Groups as a Reference!", e);
		}
		try {
			conc1.addReference(templ1);
		} catch(IllegalArgumentException e) {
			assertNull("Concepts can have one Template!",e);
		}
		try {
			conc1.addReference(templ1);
		} catch(DuplicateKeyException e) {
			assertNotNull("Concepts can just have one Template!", e);
		}
		
		//Constraints
		try {
			const1.addReference(conc1);
		} catch(IllegalArgumentException e) {
			assertNull("Constraints can have Concepts as Reference!", e);
		}
		try {
			const1.addReference(const1);
		} catch(IllegalArgumentException e) {
			assertNotNull("Constraints can not have Constraints as Reference!", e);
		}
		try {
			conc1.addReference(grp1);
		} catch(IllegalArgumentException e) {
			assertNotNull("Constraints can not have Groups as a Reference!", e);
		}
		try {
			const1.addReference(templ1);
		} catch(IllegalArgumentException e) {
			assertNull("Constraints can have one Template!", e);
		}
		try {
			const1.addReference(templ1);
		} catch(DuplicateKeyException e) {
			assertNotNull("Constraints can just have one Template!", e);
		}
		
		//Groups
		try {
			grp1.addReference(grp1);
		} catch(IllegalArgumentException e) {
			assertNull("Groups can have Groups as Reference!", e);
		}
		try {
			grp1.addReference(conc1);
		} catch(IllegalArgumentException e) {
			assertNull("Groups can have Concepts as Reference!", e);
		}
		try {
			grp1.addReference(const1);
		} catch(IllegalArgumentException e) {
			assertNull("Groups can have Constraints as Reference!", e);
		}
		try {
			grp1.addReference(templ1);
		} catch(IllegalArgumentException e) {
			assertNotNull("Groups can not have Templates as Reference!", e);
		}
		
		//Templates
		try {
			templ1.addReference(grp1);
		} catch(IllegalArgumentException e) {
			assertNotNull("Templates can not have anything as Reference!", e);
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void addReferenceNullTest(){
		conc1.addReference(null);
	}
	
	@Test
	public void getReferencedComponentsTest(){
		Set<Component> compset = new HashSet<Component>();
		
		//without references
		assertEquals("Group has no Reference!", compset, grp1.getReferencedComponents());
		assertEquals("Concept has no Reference!", compset, conc1.getReferencedComponents());
		assertEquals("Constraint has no Reference!", compset, const1.getReferencedComponents());
		assertEquals("Template has no Reference!", compset, templ1.getReferencedComponents());
		
		//with References
		//Concepts&Constraints: Reference on a Concept
		compset.add(conc1);
		conc1.addReference(conc1);
		const1.addReference(conc1);
		assertEquals("Concept has one Reference on a Concept!", compset, conc1.getReferencedComponents());
		assertEquals("Constaint has one Reference on a Concept!", compset, const1.getReferencedComponents());
		
		//Concepts&Constraints: Reference on a Constraint
		compset.add(const1);
		try {
			conc1.addReference(const1);
		} catch (IllegalArgumentException e) {
			assertNotNull("Concept should not have a Constraint as Reference!", e);
		}
		try {
			const1.addReference(const1);
		} catch (IllegalArgumentException e) {
			assertNotNull("Constraints should not have a Constraint as Reference!", e);
		}
		//Concept&Constraints: Reference on a Group
		compset.remove(const1);
		compset.add(grp1);
		try {
			conc1.addReference(grp1);
		} catch (IllegalArgumentException e) {
			assertNotNull("Concept should not have a Group as Reference!", e);
		}
		try {
			const1.addReference(grp1);
		} catch (IllegalArgumentException e) {
			assertNotNull("Constraint should not have a Group as Reference!", e);
		}
		//Groups: Reference on a Group
		compset.clear();
		compset.add(grp1);
		grp1.addReference(grp1);
		assertNotSame("Group has one Reference on a Group!", compset, grp1.getReferencedComponents());
		
		//Groups: Reference on a Constraint
		compset.add(const1);
		grp1.addReference(const1);
		assertEquals("Group has one Reference on a Constraint!", compset, grp1.getReferencedComponents());
		
		//Groups: Reference on a Concept
		compset.add(conc1);
		grp1.addReference(conc1);
		assertEquals("Group has one Reference on a Concept!", compset, grp1.getReferencedComponents());
		
		//Groups: Reference on a Template
		compset.add(templ1);
		try {
			grp1.addReference(templ1);
		} catch(IllegalArgumentException e) {
			assertNotNull("Group should not have a Template as Reference!", e);
		}
		
		//Template: References on all
		try {
			templ1.addReference(conc1);
		} catch(IllegalArgumentException e) {
			assertNotNull("Template can not have a Reference at all!", e);
		}
		try {
			templ1.addReference(grp1);
		} catch(IllegalArgumentException e) {
			assertNotNull("Template can not have a Reference at all!", e);
		}
		try {
			templ1.addReference(const1);
		} catch(IllegalArgumentException e) {
			assertNotNull("Template can not have a Reference at all!", e);
		}
		try {
			templ1.addReference(templ1);
		} catch(IllegalArgumentException e) {
			assertNotNull("Template can not have a Reference at all!", e);
		}
		
	}
	
	@Test
	public void deleteReferenceTest(){
		//Works for all Components equally
		Set<Component> compset = new HashSet<Component>();
		grp1.addReference(conc1);
		try {
			grp1.deleteReference(conc1);
		} catch (NullPointerException e) {
			assertNull(e);
		}
		assertEquals("Method delete() does not delete correctly!", compset, grp1.getReferencedComponents());
		
	}
	
	@Test(expected = NullPointerException.class)
	public void deleteReferenceNullTest(){
		conc1.deleteReference(null);
	}
	
	@Test
	public void copyTest(){
		//Works for all Components equally
		grp1.addTag("Tag1");
		conc1.copy(grp1);
		assertEquals("Method copy() does not copy all properties correctly!", grp1.getRefID(), conc1.getRefID());
		assertEquals("Method copy() does not copy all properties correctly!", grp1.getType(), conc1.getType());
		assertEquals("Method copy() does not copy all properties correctly!", grp1.getTags(), conc1.getTags());
		//Same References? Desc,Cypher,Parameters not implemented!
		
	}
	
	@Test(expected = NullPointerException.class)
	public void copyNullTest(){
		conc1.copy(null);
	}
	
}