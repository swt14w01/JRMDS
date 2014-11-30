package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import jrmds.Application;
import jrmds.main.JrmdsManagement;
import jrmds.model.*;
import junit.framework.TestCase;

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
		//delete everything
		Set<Project> p1 = jrmds.getAllProjects();
		Iterator<Project> p_iter = p1.iterator();
		while (p_iter.hasNext()) {
			Project ptemp = p_iter.next();
			Set<Component> t = ptemp.getComponents();
			Iterator<Component> t_iter = t.iterator();
			while (t_iter.hasNext()) {
				jrmds.deleteComponent(ptemp, t_iter.next());
			}
			jrmds.deleteProject(ptemp);
		}
		
		Project p = new Project("testpro");
		jrmds.saveProject(p);
				
		Component foo1 = new Concept("model:Viewblubb");
		foo1.setDescription("View blabla");
		foo1.addTag("supergeil");
		foo1.addTag("bar");
		foo1.setCypher("match (n) return n;");
		
		jrmds.saveComponent(p, foo1);
		
		//Ausgeben des aktuellen Inhaltes:
		Component foo = jrmds.getConstraint(null, null);
		foo = new Constraint("model:test");
		foo.addParameter(new Parameter("testpara",25));
		foo.addParameter(new Parameter("paralyse","beep"));
		foo.setDescription("blubbblubb");
		foo.setCypher("match (n)-[r]-() set r=n");
		foo.addTag("one");
		foo.addTag("two");
		
		jrmds.saveComponent(p, foo);
		
		
		Component foo2 = new Concept("model:Controlblubb");
		foo2.setDescription("Control blabla");
		foo2.addTag("supergeil");
		foo2.addTag("bar");
		foo2.setCypher("match (z) return n;");
		foo2.addReference(foo);
		
		jrmds.saveComponent(p, foo2);
		
		Component foo3 = new Constraint("model:Undefined");
		foo3.addParameter(new Parameter("nochnpara",3234));
		foo3.addParameter(new Parameter("meterparati","foo"));
		foo3.setDescription("another desc");
		foo3.setCypher("match (n:Component)<-[r:DEPENDSON]-(m:Component {refID:{1}})--(p:Project {name:{0}}) return n;");
		foo3.addTag("three");
		foo3.addTag("four");
		foo3.addReference(foo1);
		foo3.addReference(foo);
		
		jrmds.saveComponent(p, foo3);
	}
	

	
	@Test
	public void componentSearchTest() {
		assertNull(jrmds.getComponent(null, null));
		assertNull(jrmds.getComponent(new Project("neverexist"), new Concept("notSeen")));
		assertNull(jrmds.getComponent(new Project("testpro"), new Concept("model:test")));
		assertNull(jrmds.getComponent(new Project("test"), new Constraint("model:test")));
		assertEquals("blubbblubb",jrmds.getComponent(new Project("testpro"), new Constraint("model:test")).getDescription());
	}
	
	@Test
	public void componentRefTest() {
		Set<Component> temp = jrmds.getReferencingComponents(new Project("testpro"), new Constraint("model:test"));
		assertEquals(2,temp.size());
		Project temp2 = jrmds.getProject("testpro");
		assertTrue(jrmds.deleteComponent(temp2, jrmds.getComponent(temp2, new Constraint("model:test"))));
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
		assertEquals(3, p.getExternalRepos().size());
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
		assertFalse(jrmds.deleteProject(null));
		
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
