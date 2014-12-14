package jrmds.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jrmds.main.JrmdsManagement;
import jrmds.model.Project;
import jrmds.xml.XmlLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class ProjectController extends WebMvcConfigurerAdapter {
	@Autowired
	private JrmdsManagement controller;
	
	@Autowired
	private XmlLogic xmlLogic;

	@Autowired
	private ViewController viewController;
	
	// PROTOTYPICAL CODING HERE
	// create new project
	
	public Set<Project>projectDataRepository = new HashSet<>();

	@RequestMapping(value = "/createNewProject", method = RequestMethod.GET)
	public String createNewProject(Project newProject, Model model) {
		model.addAttribute("newProject",newProject);
		return "createNewProject";
	}

	@RequestMapping(value = "/addNewProject", method = RequestMethod.POST)
	public String addNewProject(Project newProject) {

		projectDataRepository.add(newProject);
		System.out.println(projectDataRepository.size());
		return "redirect:projects";
	}

	
/*	public String htmlOutput() {
		String temp = "";
		Set<Project> projectlist = new HashSet<Project>();
		projectlist = controller.getAllProjects();
		for (Project project : projectlist) {
			temp += project.getName();
		}

		return temp;
	}*/
	@RequestMapping(value = "/projects")
	public String projects(Model model) {
		List<Project> allProjects = new ArrayList<>();
		for(Project project : projectDataRepository) {
			allProjects.add(project);
		}
		model.addAttribute("allProjects", allProjects);
		return "projects";
	}

	@RequestMapping(value = "/projectOverview")
	public String projectOverview(Model model) {
		return "projectOverview";
	}
	
	
	// Eigentlich .POST
	@RequestMapping(value = "/projectproperties2", method = RequestMethod.GET)
	public String validateProperties() {
		
		// neuer Projectname "test"
		Project projecttest = new Project("s");
		String testname = "test";
		Set<Project> allprojects = controller.getAllProjects();
		for (Project project : allprojects) {
			if (project.getName().equals(testname))
				return "ID bereits vergeben!";
			else
				projecttest.setName(testname);
		}

		// neue Description
		// neues Member

		// neue Links
		projecttest
				.addExternalRepo("https://github.com/buschmais/jqassistant/blob/master/examples/rules/naming/jqassistant/controller.xml");
		Set<String> anotherexternalrepo = new HashSet<String>();

		anotherexternalrepo
				.add("https://github.com/buschmais/jqassistant/blob/master/examples/rules/naming/jqassistant/controller.xml");
		anotherexternalrepo
				.add("https://github.com/buschmais/jqassistant/blob/master/examples/rules/naming/jqassistant/default.xml");

		for (String anotherexternal : anotherexternalrepo) {
			if (xmlLogic.validateUrl(anotherexternal) == false)
				
				
				
				return "Error URL ext ist nicht valide!";
		}

		xmlLogic.searchForDuplicates(projecttest, anotherexternalrepo);

		// auf Zirkel kontrollieren

		return "validated";

	}
}
