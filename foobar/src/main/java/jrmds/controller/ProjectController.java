package jrmds.controller;

import java.util.Iterator;
import java.util.Set;

import jrmds.main.JrmdsManagement;
import jrmds.model.Project;
import jrmds.model.SearchRequest;
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
	
	@Autowired
	private XmlController xmlController;
	
//CREATING A NEW PROJECT "INDEX"
	@RequestMapping(value = "/createNewProject", method = {RequestMethod.GET})
	public String createNewProject(Project newProject, Model model) {
		model.addAttribute("newProject",newProject);
		return "createNewProject";
	}

//ADDING A NEW PROJECT TO THE DATABASE
	@RequestMapping(value = "/addNewProject", method = RequestMethod.POST)
	public String addNewProject(Project newProject) {
		jrmds.saveProject(newProject);
		return "redirect:projects";
	}
	
//LIST OF ALL PROJECTS EXISTING
	@RequestMapping(value="/projects", method={RequestMethod.POST, RequestMethod.GET})
	public String projects(
	Model model, SearchRequest searchRequest){
		Set<Project> projects = jrmds.getAllProjects();
		model.addAttribute("projects", projects);
		return "projects";
	}

	@RequestMapping(value = "/projectOverview", method={RequestMethod.POST, RequestMethod.GET})
	public String projectOverview(
			@RequestParam(required=true) String project,
			Model model, SearchRequest searchRequest) {
		Project p = jrmds.getProject(project);
		model.addAttribute("project",p);
		return "projectOverview";
	}
	
//ProjectProperties "INDEX"
	@RequestMapping(value = "/projectProps", method = RequestMethod.GET)
	public String showProperties(@RequestParam(required=true) String project, Model model) {
		Project p = jrmds.getProject(project);
		model.addAttribute("project" , p);
		return "projectProps";
	}

//SAVING NAME AND DESCRIPTION OF A PROJECT
	@RequestMapping(value ="/saveProps", method = RequestMethod.POST)
	public String saveProps(@RequestParam(required = true) String project, @RequestParam String name, @RequestParam String description, Model model){
		String msg ="";
		Project p = jrmds.getProject(project);
		
		
		//If new name == old name
		if((name.equals(p.getName()))) msg="The Project name didn't change!\n";
		//If ner name != old name
		else {
			Set<Project> allprojects = jrmds.getAllProjects();
			Iterator<Project> iter = allprojects.iterator();
			//checking if name already exists in database
			Boolean nameexists = false;
			while (iter.hasNext()) {
				Project next = iter.next();
				if(next.getName().equals(name)){
				nameexists = true;
				break;
				}
			}
			//if Name is unique
			if (!nameexists){
				p.setName(name);
				jrmds.saveProject(p);
				msg ="Project name successfully changed to " + name + ".";
			}
			else msg="The Project name already exists!";
		}
		//if description changed
		if(!description.equals(p.getDescription())){
			p.setDescription(description);
			jrmds.saveProject(p);
			msg = msg + "Description successfully changed.";
		}

		model.addAttribute("message",msg);
		model.addAttribute("linkRef","/projectProps?project="+name);
		model.addAttribute("linkPro","/projectOverview?project="+name);
		
		return "confirmation";
	}
	
	
	@RequestMapping(value ="/saveMembers", method = RequestMethod.POST)
	public String editMembers(@RequestParam(required = true) String  project){
		Project p = jrmds.getProject(project);
		return "redirect:projectProbs(project=${p.getName()})";
	}

//ADDING A EXTERNAL REPOSITIRY TO A PROJECT WITHOUT CHECK!
	@RequestMapping(value ="/addExternalRepos", method = RequestMethod.POST)
	public String addExternalRepos(@RequestParam(required = true) String  project, @RequestParam String externalrepo, Model model){
		Project p = jrmds.getProject(project);
		
		
		//Checks if XML is valid
		//Boolean extrepovalide = xmlController.validateUrl(externalrepo);
		Boolean extrepovalide = true;
		
		String msg = "";
		if(!extrepovalide) msg="The External Repository is not a valid xml!";
		else {
			Set<String> externalrepolist = p.getExternalRepos();
			
			Boolean repoexists = false;
			
			if(externalrepolist != null){
				Iterator<String> iter = externalrepolist.iterator();
				while(iter.hasNext()){
					String next = iter.next();
					if(next.equals(externalrepo)){ 
						repoexists = true;
						break;}
				}
			}
			
			if(!repoexists){
				p.addExternalRepo(externalrepo);
				jrmds.saveProject(p);
				msg = "New Repository successfully added!";
			}
			else msg = "The Repository already exists."; 
		}
		
		model.addAttribute("message",msg);
		model.addAttribute("linkRef","/projectProps?project="+p.getName());
		model.addAttribute("linkPro","/projectOverview?project="+p.getName());
		
		return "confirmation";
	}
	
//TODO: DELETING EXTERNAL REPOSITORIES FROM A PROJECT
	@RequestMapping(value ="/deleteExternalRepos", method = RequestMethod.POST)
	public String deleteExternalRepos(@RequestParam(required = true) String  project){
		Project p = jrmds.getProject(project);
		return "redirect:projectProbs(project=${p.getName()})";
	}
	
//DELETING PROJECT
	@RequestMapping(value ="/deleteProject", method = RequestMethod.POST)
	public String deleteProject(@RequestParam(required = true) String  project){
		Project p = jrmds.getProject(project);
		jrmds.deleteProject(p);
		return "redirect:projects";
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
