package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import jrmds.controller.ProjectController;
import jrmds.model.Component;
import jrmds.model.Group;
import jrmds.model.Project;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectTest {
	@Autowired
	ProjectController pc;

	
		Project proj1 = new Project("project1");
		Project proj2 = new Project("project2");
		Project proj3 = new Project("project3");
		Component comp1 = new Group("group1");
		Component comp2 = new Group("group2");
		
		Set<String> externalRepos1 = new HashSet<String>();
		Set<Component> componentSet1 = new HashSet<Component>();
		
		
	@Test
	public void getNameTest() {
		assertEquals("getName() method is not correct!","project1", proj1.getName());
	}
	
	@Test
	public void getExternalRepoTest(){	
		assertEquals("getExternalRepo() method is not correct!",externalRepos1,proj1.getExternalRepos());
	}
	
	@Test
	public void getComponentsTest(){
		assertEquals("getExternalRepo() method is not correct!",componentSet1,proj1.getComponents());
	}
	
	@Test
	public void setIdTest() {
		proj1.setId(3L);
		assertEquals("setId() method is not correct!", (Long) 3L, proj1.getId());
	}
	
	
	@Test
	public void setNameTest(){
		proj1.setName("test");
		assertEquals("setName() method is not correct!","test", proj1.getName());
	}
	

	@Test
	public void addExternalRepoTest(){
		externalRepos1.add("test");
		proj1.addExternalRepo("test");
		assertEquals("addExternalRepo() method is not correct!", externalRepos1, proj1.getExternalRepos());
	}
	
	@Test
	public void setExternalRepoTest(){
		externalRepos1.add("test");
		externalRepos1.add("test2");
		proj1.setExternalRepo(externalRepos1);
		assertEquals("setExternalRepo() method is not correct!", externalRepos1, proj1.getExternalRepos());
		
	}
	
	@Test
	public void deleteExternalRepoTest() {
		externalRepos1.remove("test");
		proj1.deleteExternalRepo("test");
		assertEquals("deleteExternalRepo() method is not correct!", externalRepos1, proj1.getExternalRepos());
	}
	
	@Test
	public void addComponentTest() {
		
		assertFalse("Method addComponent() has to return false when trying to add null!",proj1.addComponent(null));
		
		assertTrue("Method addComponent() has to return true when adding a component!",proj1.addComponent(comp1));
		
		componentSet1.add(comp1);
		
		assertEquals("Method addComponent() doesn't add the right component!", componentSet1, proj1.getComponents());
	}
	
	@Test
	public void deleteComponentTest() {
		proj1.addComponent(comp1);
		assertFalse("Method deleteComponent() has to return false when trying to delete null!",proj1.deleteComponent(null));
		assertTrue("Method deleteComponent() has to return true when trying to delete a Component which is not contained!",proj1.deleteComponent(comp2));
		
		assertTrue("Method deleteComponent() has to return true when deleting a contained component!",proj1.deleteComponent(comp1));
		
		assertEquals("Method deleteComponent() doesn't delete the right component!", componentSet1, proj1.getComponents());
	}

	@Test
	public void copyProjectTest() {
		
		//copy into an empty project
		proj1.addComponent(comp1);
		proj1.addExternalRepo("external1");
		proj2.copyProject(proj1);
		
		assertEquals("The method copyProject() doesn't copy the name correctly!", proj1.getName(), proj2.getName());
		assertEquals("The method copyProject() doesn't copy the Id correctly!",proj1.getId(), proj2.getId());
		assertEquals("The method copyProject() doesn't copy the ExternalRepo correctly!",proj1.getExternalRepos(), proj2.getExternalRepos());
		assertEquals("The method copyProject() doesn't copy the ComponentSet correctly!",proj1.getComponents(), proj2.getComponents());
		
		//copy into a project with components
		proj3.addComponent(comp2);
		proj3.addExternalRepo("external2");
		proj1.copyProject(proj3);
		componentSet1.add(comp1);
		componentSet1.add(comp2);
		
		assertEquals("In a project that already has components, the new components have to be added!",proj1.getComponents(), componentSet1);		
	}
	
	@Test
	public void equalsTest() {
		proj1.setId(1L);
		proj2.setId(1L);
		proj3.setId(2L);
		
		assertTrue("Two identical Ids have to return true!",proj1.equals(proj2));
		assertFalse("Two different Ids have to return false!", proj1.equals(proj3));
	}
	
	@Test
	public void hashCodeTest() {
		proj1.setId(1L);
		//	assertEquals("Method hashCode doesn't convert correctly!", 1, proj1.hashCode());
	}
	
	@Test
	public void toStringTest(){
		assertEquals("Method toString() doesn't return correctly!","project1", proj1.toString());
	}

}