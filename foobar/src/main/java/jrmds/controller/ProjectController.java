package jrmds.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jrmds.main.JrmdsManagement;
import jrmds.model.Project;
import jrmds.xml.XmlController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class ProjectController extends WebMvcConfigurerAdapter {
	@Autowired
	private JrmdsManagement jrmds;

	@Autowired
	private ViewController viewController;
	
	@RequestMapping(value = "/createNewProject", method = {RequestMethod.GET})
	public String createNewProject(Project newProject, Model model) {
		model.addAttribute("newProject",newProject);
		return "createNewProject";
	}

	@RequestMapping(value = "/addNewProject", method = RequestMethod.POST)
	public String addNewProject(Project newProject) {
		jrmds.saveProject(newProject);
		return "redirect:projects";
	}
	
	@RequestMapping(value="/projects", method={RequestMethod.POST, RequestMethod.GET})
	public String projects(
	Model model){
		Set<Project> projects = jrmds.getAllProjects();
		model.addAttribute("projects", projects);
		return "projects";
	}

	@RequestMapping(value = "/projectOverview", method={RequestMethod.POST, RequestMethod.GET})
	public String projectOverview(
			@RequestParam(required=true) Project project,
			Model model) {
		model.addAttribute("project",project);
		return "projectOverview";
	}
	
	@RequestMapping(value = "/projectProps", method = RequestMethod.GET)
	public String showProperties(@RequestParam(required=true) Project project, Model model) {
		model.addAttribute("project",project);
		return "projectProps";
	}
	
	@RequestMapping(value ="/editProjectProps", method = RequestMethod.POST)
	public String editProperties(@RequestParam(required = true) Project project, Model model){
		model.addAttribute("project",project);
		return "projectProbs";
	}
	
	/* Eigentlich .POST
	@RequestMapping(value = "/projectproperties2", method = RequestMethod.GET)
	public String validateProperties() {
		XmlController xmlcontrol = new XmlController();

		// neuer Projectname "test"
		Project projecttest = new Project("s");
		String testname = "test";
		Set<Project> allprojects = jrmds.getAllProjects();
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
			if (xmlcontrol.validateUrl(anotherexternal) == false)
				return "Error URL ext ist nicht valide!";
		}

		xmlcontrol.searchForDuplicates(projecttest, anotherexternalrepo);

		// auf Zirkel kontrollieren

		return "validated";
*/
	
}
