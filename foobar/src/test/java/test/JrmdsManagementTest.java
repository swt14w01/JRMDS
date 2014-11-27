package test;

import static org.junit.Assert.*;
import jrmds.Application;
import jrmds.main.JrmdsManagement;
import jrmds.model.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
@Transactional
public class JrmdsManagementTest {
	@Autowired
	JrmdsManagement jrmds;
	
	@Before
	public void startup() {
		
	}
	
	@Test
	public void projectSaverTest() {
		assertFalse(jrmds.saveProject(null));
		
		Project p = new Project("test123");
		assertTrue(jrmds.saveProject(p));
		
		Project p2 = new Project("test123");
		assertTrue(jrmds.saveProject(p2));
	}
	
	@Test
	public void projectUpdateTest() {
		Project p = new Project("foobar");
		jrmds.saveProject(p);
		
		p.addExternalRepo("someRepo");
		p.addExternalRepo("anotherRepo");
		p.addExternalRepo("barbier");
		p.addExternalRepo("beeper");
		p.addExternalRepo("beeper");
		assertTrue(jrmds.saveProject(p));
		
		p = jrmds.getProject("foobar");
		assertEquals(4, p.getExternalRepos().size());
		
		assertTrue(p.deleteExternalRepo("anotherRepo"));
		assertTrue(jrmds.saveProject(p));
	}
	
	@Test
	public void projectGetterTest() {
		assertNull(jrmds.getProject(null));
		
		Project p = new Project("testpro");
		jrmds.saveProject(p);		
		assertEquals(p.getName(), jrmds.getProject("testpro").getName());
		
		//partial Names shouldn't return any project, projectName must be fully qualified
		assertNull(jrmds.getProject("test"));
	}
	
}
