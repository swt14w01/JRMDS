package test;

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
//@Transactional
public class JrmdsManagementTest extends TestCase {
	@Autowired
	JrmdsManagement jrmds;
	
	@Before
	public void setUp() {
		//delete everything
		Set<Project> p1 = jrmds.getAllProjects();
		Iterator<Project> p_iter = p1.iterator();
		while (p_iter.hasNext()) {			
			jrmds.deleteProject(p_iter.next());
		}
		//List<Component> c = jrmds.getAllComponents();
		//Iterator<Component> c_iter = c.iterator();
		//while(c_iter.hasNext()) jrmds.deleteComponent(null, c_iter.next());
		

		Project p = jrmds.getProject("testpro");
		if (p == null) { 
			p =  new Project("testpro");
			jrmds.saveProject(p);
		}
		
		Component foo1 = new Concept("model:Viewblubb");
		foo1.setDescription("View blabla");
		foo1.addTag("supergeil");
		foo1.addTag("bar");
		foo1.setCypher("match (n) return n;");
		jrmds.saveComponent(p, foo1);
		
		Component foo = new Concept("model:test");
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
		jrmds.addComponentRef(p, foo2, foo);
		jrmds.saveComponent(p, foo2);
		
		Component foo3 = new Concept("model:zucker");
		foo3.setDescription("zuviel Zucker ist schlecht");
		foo3.setCypher("match (n)-[r]-(m:zucker) return n");
		foo3.addTag("one");
		foo3.addTag("two");
		foo3.addTag("three");
		jrmds.addComponentRef(p, foo3, foo2);
		jrmds.saveComponent(p, foo3);
		
		Component foo4 = new Concept("model:Apfel");
		foo4.setDescription("Äppel sind oko");
		foo4.setCypher("match (n)-[r:appel]-(m) return n");
		foo4.addTag("four");
		foo4.addTag("two");
		foo4.addTag("three");
		jrmds.addComponentRef(p, foo4, foo3);
		jrmds.addComponentRef(p, foo4, foo2);
		jrmds.saveComponent(p, foo4);
		
		Component foo5 = new Constraint("model:Undefined");
		foo5.addParameter(new Parameter("nochnpara",3234));
		foo5.addParameter(new Parameter("meterparati","foo"));
		foo5.setDescription("another desc");
		foo5.setCypher("match (n:Component)<-[r:DEPENDSON]-(m:Component {refID:{1}})--(p:Project {name:{0}}) return n;");
		foo5.addTag("three");
		foo5.addTag("four");
		jrmds.addComponentRef(p, foo5, foo1);
		jrmds.addComponentRef(p, foo5, foo);
		jrmds.addComponentRef(p, foo5, foo3);
		jrmds.saveComponent(p, foo5);
		
		
		
		Component fastcheck = new Group("fastcheck");
		fastcheck.addTag("schnellCheck");
		jrmds.addComponentRef(p, fastcheck, foo);
		jrmds.addComponentRef(p, fastcheck, foo1);
		jrmds.saveComponent(p,fastcheck);
		
		Component slowcheck = new Group("slowychecky");
		slowcheck.addTag("schneckencheck");
		jrmds.addComponentRef(p, slowcheck, fastcheck);
		jrmds.addComponentRef(p, slowcheck, foo5);
		jrmds.addComponentRef(p, slowcheck, foo4);
		jrmds.saveComponent(p, slowcheck);
		
	}
/*
	@Test
	public void getGroupNodes() {
		Project p = jrmds.getProject("testpro");
		Group g = jrmds.getGroup(p, "fastcheck");
		Set<Component> t1 = jrmds.getProjectComponents(p);
		assertEquals(8,t1.size());
		
		Set<Component> temp = jrmds.getGroupComponents(p, g);
		String foo="";
		Iterator<Component> iter = temp.iterator();
		while (iter.hasNext()) {
			foo+=iter.next().getRefID();
		}
		assertEquals("blubb",foo);

	}*/
	
	@Test
	public void blubb() {
		assertNull(null);
	}
	
	@Test
	public void identicalRefIDinAnotherProject() {
		Project p = new Project("thisIsaCopy");
		jrmds.saveProject(p);
		
		Component foo1 = new Concept("model:Viewblubb");
		foo1.setDescription("View blabla");
		foo1.addTag("supergeil");
		foo1.addTag("bar");
		foo1.setCypher("match (n) return n;");
		assertTrue(jrmds.saveComponent(p, foo1));
		
		Component foo = new Concept("model:test");
		foo.addParameter(new Parameter("testpara",25));
		foo.addParameter(new Parameter("paralyse","beep"));
		foo.setDescription("blubbblubb");
		foo.setCypher("match (n)-[r]-() set r=n");
		foo.addTag("one");
		foo.addTag("two");
		
		jrmds.saveComponent(p, foo);
		
		
		Component bar2 = new Constraint("model:Controlblubb");
		bar2.setDescription("Control bloblo");
		bar2.addTag("superöde");
		bar2.addTag("apb");
		bar2.setCypher("match (pi) return euler;");
		assertTrue(jrmds.addComponentRef(p, bar2, foo));
		jrmds.saveComponent(p, bar2);
		
		assertFalse(jrmds.addComponentRef(p, foo, bar2));
		
		Component bar1 = jrmds.getComponent(p, bar2);
		assertEquals(bar2.getCypher(), bar1.getCypher());
	}
	
	@Test
	public void componentSearchTest() {
		assertNull(jrmds.getComponent(null, null));
		assertNull(jrmds.getComponent(new Project("neverexist"), new Constraint("notSeen")));
		assertNull(jrmds.getComponent(new Project("testpro"), new Constraint("model:test")));
		assertNull(jrmds.getComponent(new Project("test"), new Concept("model:test")));
		assertEquals("blubbblubb",jrmds.getComponent(new Project("testpro"), new Concept("model:test")).getDescription());
	}
	
	@Test
	public void componentRefTest() {
		Set<Component> temp = jrmds.getReferencingComponents(new Project("testpro"), new Concept("model:test"));
		assertEquals(3,temp.size());
		Project temp2 = jrmds.getProject("testpro");
		Component temp3 = jrmds.getComponent(temp2, new Concept("model:test"));
		assertEquals("blubbblubb", temp3.getDescription());
		assertTrue(jrmds.deleteComponent(temp2, temp3));
		assertNull(jrmds.getComponent(temp2, new Concept("model:test")));
	}

	@Test
	public void projectSaverTest() {
		assertFalse(jrmds.saveProject(null));
		
		Project p = new Project("test123");
		assertTrue(jrmds.saveProject(p));
		
		Project p2 = new Project("test123");
		assertTrue(jrmds.saveProject(p2));
		
		assertNotNull(jrmds.getProject("test123"));
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
		
		assertTrue(jrmds.deleteProject(p));
	}
	
	@Test
	public void projectGetterTest() {
		assertNull(jrmds.getProject(null));
		
		Project p = new Project("testpro");
		jrmds.saveProject(p);		
		assertEquals(p.getName(), jrmds.getProject("testpro").getName());
		
		//partial Names shouldn't return any project, projectName must be fully qualified
		assertNull(jrmds.getProject("another"));
	}
	
}
