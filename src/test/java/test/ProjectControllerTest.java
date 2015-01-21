package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jrmds.controller.ProjectController;
import jrmds.model.Component;
import jrmds.model.Group;

import org.junit.Test;


public class ProjectControllerTest {
	ProjectController pc = new ProjectController();
	Component grp1 = new Group("group1");
	Component grp2 = new Group("group2");
	Component grp3 = new Group("group3");
	Component grp4 = new Group("group4");
	Component grp5 = new Group("group5");
	
	@Test(expected=IllegalArgumentException.class)
	public void depthSearchwithCycleTest(){
		System.out.println("TEST");
		grp3.addReference(grp4);
		grp4.addReference(grp5);
		grp5.addReference(grp3);
		grp1.addReference(grp2);
		
		Set<Component> newRepo = new HashSet<Component>();
		newRepo.add(grp1);
		newRepo.add(grp2);
		newRepo.add(grp3);
		newRepo.add(grp4);
		newRepo.add(grp5);
		
		Map<String, Boolean> visited = new HashMap<String, Boolean>();
		
		for(Component nr:newRepo){
			visited.put(nr.getRefID(), false);
		}
		
		for(Component nr:newRepo){
		System.out.println("STEP");
		pc.depthSearch(nr, visited);
		}
	}
		
	
	@Test
	public void depthSearchWithoutCycleTest(){
		grp1.addReference(grp2);
		grp2.addReference(grp3);
		grp3.addReference(grp4);
		grp5.addReference(grp1);
		
		Set<Component> newRepo = new HashSet<Component>();
		newRepo.add(grp1);
		newRepo.add(grp2);
		newRepo.add(grp3);
		newRepo.add(grp4);
		newRepo.add(grp5);
		Map<String, Boolean> visited = new HashMap<String, Boolean>();
		
		for(Component nr:newRepo){
			visited.put(nr.getRefID(), false);
		}
		
		for(Component nr:newRepo){
		pc.depthSearch(nr, visited);
		}
	}
}
