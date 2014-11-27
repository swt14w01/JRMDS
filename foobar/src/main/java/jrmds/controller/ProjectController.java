package jrmds.controller;


import jrmds.main.JrmdsManagement;
import jrmds.model.Project;
import jrmds.xml.XmlController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

@RestController
public class ProjectController {
	@Autowired
	private JrmdsManagement controller;
	
	@RequestMapping(value = "/projects")
	public String htmlOutput() {
		String temp = "";
		Set<Project> projectlist = new HashSet<Project>();
		projectlist = controller.getAllProjects();
		for (Project project : projectlist) {
			temp += project.getName();
		}

		return temp;
	}


	//Eigentlich .POST
	@RequestMapping(value ="/projectproperties2" ,  method = RequestMethod.GET)
	public String validateProperties(){
		 XmlController xmlcontrol = new XmlController();
		
		
		 //neuer Projectname "test"
		Project projecttest = new Project("s");
		String testname = "test";
		Set<Project> allprojects = controller.getAllProjects();
		for (Project project : allprojects){
			if(project.getName().equals(testname)) return "ID bereits vergeben!";	
			else  projecttest.setName(testname);
		}
		
		//neue Description
		//neues Member
	
		//neue Links
		projecttest.addExternalRepo("https://github.com/buschmais/jqassistant/blob/master/examples/rules/naming/jqassistant/controller.xml");
		List<String> anotherexternalrepo = new ArrayList<String>();
		
		anotherexternalrepo.add("https://github.com/buschmais/jqassistant/blob/master/examples/rules/naming/jqassistant/controller.xml");
		anotherexternalrepo.add("https://github.com/buschmais/jqassistant/blob/master/examples/rules/naming/jqassistant/default.xml");
		
		for (String anotherexternal : anotherexternalrepo){
			if (xmlcontrol.validateUrl(anotherexternal) ==false) return "Error URL ext ist nicht valide!";
		}
		
		xmlcontrol.searchForDuplicates(projecttest , anotherexternalrepo);
			
		//auf Zirkel kontrollieren
		
		return "validated";
	
	}
}
