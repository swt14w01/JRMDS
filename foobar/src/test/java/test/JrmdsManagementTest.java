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
	public void projectDeleterTest() {
		Project p = new Project("toDelete");
		Project p2 = new Project("toDeleteEither");
		jrmds.saveProject(p);
		jrmds.saveProject(p2);
		
		assertNotNull(jrmds.getProject(p.getName()));
		
		assertTrue(jrmds.deleteProject(p));
		assertNull(jrmds.getProject(p.getName()));
		
		p = jrmds.getProject(p2.getName());
		assertTrue(jrmds.deleteProject(p));
		assertNull(jrmds.getProject(p2.getName()));
		
		assertFalse(jrmds.deleteProject(new Project("blubb")));
	}
	
	@Test
	public void projectRefDeleterTest() {
		Project p = new Project("toDelete2");
		jrmds.saveProject(p);
		
		Component foo1 = new Concept("model:Viewblubb");
		foo1.setDescription("View blabla");
		foo1.addTag("supergeil");
		foo1.addTag("bar");
		foo1.setCypher("match (n) return n;");
		
		p = jrmds.getProject(p.getName());
		jrmds.saveComponent(p, foo1);
		
		assertFalse(jrmds.deleteProject(p));
		
		p.deleteComponent(foo1);
		jrmds.saveProject(p);
		
		assertTrue(jrmds.deleteProject(p));
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
