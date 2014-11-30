package jrmds.controller;

import java.util.Iterator;
import java.util.Set;

import jrmds.main.JrmdsManagement;
import jrmds.model.Component;
import jrmds.model.Concept;
import jrmds.model.Constraint;
import jrmds.model.Parameter;
import jrmds.model.Project;
import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComponenController {
	@Autowired
	private JrmdsManagement controller;
	@Autowired
	private UserManagement usr;
	
	@RequestMapping(value="/editRules")
	public String htmlRules() {
		String temp="";
		Project p = new Project("testpro");
		controller.saveProject(p);
				
		Component foo1 = new Concept("model:Viewblubb");
		foo1.setDescription("View blabla");
		foo1.addTag("supergeil");
		foo1.addTag("bar");
		foo1.setCypher("match (n) return n;");
		
		controller.saveComponent(p, foo1);
		
		//Ausgeben des aktuellen Inhaltes:
		Component foo = controller.getConstraint(null, null);
		foo = new Constraint("model:test");
		foo.addParameter(new Parameter("testpara",25));
		foo.addParameter(new Parameter("paralyse","beep"));
		foo.setDescription("blubbblubb");
		foo.setCypher("match (n)-[r]-() set r=n");
		foo.addTag("one");
		foo.addTag("two");
		
		controller.saveComponent(p, foo);
		
		
		Component foo2 = new Concept("model:Controlblubb");
		foo2.setDescription("Control blabla");
		foo2.addTag("supergeil");
		foo2.addTag("bar");
		foo2.setCypher("match (z) return n;");
		foo2.addReference(foo);
		
		controller.saveComponent(p, foo2);
		
		Component foo3 = new Constraint("model:Undefined");
		foo3.addParameter(new Parameter("nochnpara",3234));
		foo3.addParameter(new Parameter("meterparati","foo"));
		foo3.setDescription("another desc");
		foo3.setCypher("match (n:Component)<-[r:DEPENDSON]-(m:Component {refID:{1}})--(p:Project {name:{0}}) return n;");
		foo3.addTag("three");
		foo3.addTag("four");
		foo3.addReference(foo1);
		foo3.addReference(foo);
		
		controller.saveComponent(p, foo3);
		
		//suche alle Parameter zusammen
		Set<Parameter> bar = foo.getParameters();
	
		Iterator<Parameter> iter = bar.iterator();
		while (iter.hasNext()) {
			temp+=iter.next().getName();
		}
		
		//Suche alle Dependencies (also Rules, von denen diese Rule hier abhängt) ... ist in dem Objekt drin, Set DEPENDSON;
		
		//welche Rules hängen von dieser ab... referencedBy()
		
		return temp;
	}
	
	@RequestMapping(value="/editGroup")
	public String htmlGroup() {
		
		
		return "";
	}
	
	@RequestMapping(value="/editTemplate")
	public String htmlTemplate() {
		
		
		return "";
	}
}
