package test;

import java.util.Iterator;
import java.util.Set;

import jrmds.Application;
import jrmds.main.JrmdsManagement;
import jrmds.model.Component;
import jrmds.model.Concept;
import jrmds.model.Constraint;
import jrmds.model.Group;
import jrmds.model.Parameter;
import jrmds.model.Project;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
//@Transactional
public class JrmdsManagementTest extends TestCase {
	@Autowired
	JrmdsManagement jrmds;
	private static boolean setupDone = false; 
	
	@Before
	public void setUp() {
		if (setupDone) return;
		setupDone = true;
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
			p = jrmds.saveProject(p);
		}
		
		Component foo1 = new Concept("model:Viewblubb");
		foo1.setDescription("View blabla");
		foo1.addTag("supergeil");
		foo1.addTag("bar");
		foo1.setCypher("match (n) return n;");
		foo1 = jrmds.saveComponent(p, foo1);
		
		Component foo = new Concept("model:test");
		foo.addParameter(new Parameter("testpara",25));
		foo.addParameter(new Parameter("paralyse","beep"));
		foo.setDescription("blubbblubb");
		foo.setCypher("match (n)-[r]-() set r=n");
		foo.addTag("one");
		foo.addTag("two");
		foo = jrmds.saveComponent(p, foo);
		
		Component foo2 = new Concept("model:Controlblubb");
		foo2.setDescription("Control blabla");
		foo2.addTag("supergeil");
		foo2.addTag("bar");
		foo2.setCypher("match (z) return n;");
		jrmds.addComponentRef(p, foo2, foo);
		foo2 = jrmds.saveComponent(p, foo2);
		
		Component foo3 = new Concept("model:zucker");
		foo3.setDescription("zuviel Zucker ist schlecht");
		foo3.setCypher("match (n)-[r]-(m:zucker) return n");
		foo3.addTag("one");
		foo3.addTag("two");
		foo3.addTag("three");
		jrmds.addComponentRef(p, foo3, foo2);
		foo3 = jrmds.saveComponent(p, foo3);
		
		Component foo4 = new Concept("model:Apfel");
		foo4.setDescription("Äppel sind oko");
		foo4.setCypher("match (n)-[r:appel]-(m) return n");
		foo4.addTag("four");
		foo4.addTag("two");
		foo4.addTag("three");
		jrmds.addComponentRef(p, foo4, foo3);
		jrmds.addComponentRef(p, foo4, foo2);
		foo4 = jrmds.saveComponent(p, foo4);
		
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
		foo5 = jrmds.saveComponent(p, foo5);
		
		Group fastcheck = new Group("fastcheck");
		fastcheck.addTag("schnellCheck");
		fastcheck.addTag("test");
		jrmds.addGroupRef(p, fastcheck, foo, "high");
		jrmds.addGroupRef(p, fastcheck, foo1, "low");
		jrmds.saveComponent(p,fastcheck);
		//fastcheck = jrmds.getGroup(p, "fastcheck");
		
		Group slowcheck = new Group("slowychecky");
		slowcheck.addTag("schneckencheck");
		jrmds.addComponentRef(p, slowcheck, fastcheck);
		jrmds.addGroupRef(p, slowcheck, foo5, "high");
		jrmds.addGroupRef(p, slowcheck, foo4, "ultra");
		slowcheck = new Group(jrmds.saveComponent(p, slowcheck));
		
	}
	
	@Test
	public void orphanedInodes() {
		Project p = jrmds.getProject("testpro");
		Group g1 = jrmds.getGroup(p, "slowychecky");
		Group g2 = jrmds.getGroup(p, "fastcheck");
		
		//every component referenced by slowcheck should be orphaned
		Set<Component> t1 = jrmds.getSingleReferencedNodes(p, g1);
		assertEquals(3,t1.size());
		
		//every component referenced by fastcheck has other upstream nodes
		Set<Component> t2 = jrmds.getSingleReferencedNodes(p, g2);
		assertEquals(0,t2.size());
	}

	@Test
	public void getGroupNodes() {
		Project p = jrmds.getProject("testpro");
		Group g = jrmds.getGroup(p, "slowychecky");
		Set<Component> t1 = jrmds.getProjectComponents(p);
		assertEquals(8,t1.size());
		
		Set<Component> temp = jrmds.getGroupComponents(p, g);
		//String foo="";
		//Iterator<Component> iter = temp.iterator();
		//while (iter.hasNext()) {
		//	Component t = iter.next();
		//	foo+="|-" + t.getId().toString() + "_" + t.getRefID() + "|";
		//}
		assertEquals(8,temp.size());

	}
	
	@Test
	public void upstreamTest() {
		//check for Upstream Nodes
		Project p = jrmds.getProject("testpro");
		Group g = jrmds.getGroup(p, "fastcheck");
		assertEquals(1,jrmds.getReferencingComponents(p, g).size());
	}
	
	@Test
	public void downstreamTest() {
		Project p = jrmds.getProject("testpro");
		Group g = jrmds.getGroup(p, "slowychecky");
		assertEquals(3,g.getReferencedComponents().size());
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
		assertEquals(foo1.getRefID(),jrmds.saveComponent(p, foo1).getRefID());
		
		Component foo = new Concept("model:test");
		foo.addParameter(new Parameter("testpara",25));
		foo.addParameter(new Parameter("paralyse","beep"));
		foo.setDescription("blubbblubb");
		foo.setCypher("match (n)-[r]-() set r=n");
		foo.addTag("one");
		foo.addTag("two");
		
		foo = jrmds.saveComponent(p, foo);
		
		
		Component bar2 = new Constraint("model:Controlblubb");
		bar2.setDescription("Control bloblo");
		bar2.addTag("superöde");
		bar2.addTag("apb");
		bar2.setCypher("match (pi) return euler;");
		try {		
			jrmds.addComponentRef(p, bar2, foo);
		} catch (Exception e) {
			assertNull(e);
		}
		jrmds.saveComponent(p, bar2);
		
		try {
			jrmds.addComponentRef(p, foo, bar2);
		} catch (IllegalArgumentException e) {
			assertNotNull("You shouldnt be able to create a cycle!",e);
		}
		
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
		//jrmds.deleteComponent(temp2, temp3);
		//assertNull(jrmds.getComponent(temp2, new Concept("model:test")));
	}

	@Test
	public void projectSaverTest() {
		try {
			jrmds.saveProject(null);
		} catch (NullPointerException e) {
			assertNotNull(e);
		}
		
		Project p = new Project("test123");
		assertNotNull(jrmds.saveProject(p));
		
		Project p2 = new Project("test123");
		assertNotNull(jrmds.saveProject(p2));
		
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
		p = jrmds.saveProject(p);
		
		p = jrmds.getProject("foobar");
		assertEquals(4, p.getExternalRepos().size());
		
		assertTrue(p.deleteExternalRepo("anotherRepo"));
		p = jrmds.saveProject(p);
		assertEquals(3, p.getExternalRepos().size());
	}
	
	@Test
	public void projectDeleterTest() {
		Project p = new Project("toDelete");
		Project p2 = new Project("toDeleteEither");
		jrmds.saveProject(p);
		jrmds.saveProject(p2);
		
		assertNotNull(jrmds.getProject(p.getName()));
		
		jrmds.deleteProject(p);
		assertNull(jrmds.getProject(p.getName()));
		
		p = jrmds.getProject(p2.getName());
		jrmds.deleteProject(p);
		assertNull(jrmds.getProject(p2.getName()));
		
		try {
			jrmds.deleteProject(new Project("blubb"));
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	@Test
	public void projectRefDeleterTest() {
		try {
			jrmds.deleteProject(null);
		} catch (NullPointerException e) {
			assertNotNull(e);
		}
		
		Project p = new Project("toDelete2");
		jrmds.saveProject(p);
		
		Component foo1 = new Concept("model:Viewblubb");
		foo1.setDescription("View blabla");
		foo1.addTag("supergeil");
		foo1.addTag("bar");
		foo1.setCypher("match (n) return n;");
		
		p = jrmds.getProject(p.getName());
		jrmds.saveComponent(p, foo1);
		
		try {
			jrmds.deleteProject(p);
		} catch (Exception e) {
			assertNull(e);
		}
	}
	
	@Test
	public void projectGetterTest() {
		assertNull(jrmds.getProject(null));
		
		Project p = new Project("test1234");
		jrmds.saveProject(p);		
		assertEquals(p.getName(), jrmds.getProject("test1234").getName());
		
		//partial Names shouldn't return any project, projectName must be fully qualified
		assertNull(jrmds.getProject("test"));
	}
	
}
