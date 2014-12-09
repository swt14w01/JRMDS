package jrmds.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jrmds.main.JrmdsManagement;
import jrmds.model.Component;
import jrmds.model.ComponentType;
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
	Model model){
		Set<Project> projects = jrmds.getAllProjects();
		model.addAttribute("projects", projects);
		return "projects";
	}

// OVERVIEW OF ONE SELECTED PROJECTS
// DISPLAYING PROJECTS CONTENT
	
	@RequestMapping(value = "/projectOverview", method={RequestMethod.POST, RequestMethod.GET})
	public String projectOverview(
			@RequestParam(required=true) String project,
			Model model) {
		
		Map<Component, String>resultGroups = new HashMap<>();
		Map<Component, String>resultConcepts = new HashMap<>();
		Map<Component, String>resultConstraints = new HashMap<>();
		Map<Component, String>resultQueryTemplates = new HashMap<>();
		Project projectToBeDisplayed = jrmds.getProject(project);
		
		
		 for(Component component : projectToBeDisplayed.getComponents()) {
		  switch (component.getType()) {
					case GROUP:
						resultGroups.put(component, jrmds.getComponentAssociatedProject(component).getName());
						break;
					case CONCEPT:
						resultConcepts.put(component, jrmds.getComponentAssociatedProject(component).getName());
						break;
					case CONSTRAINT:
						resultConstraints.put(component, jrmds.getComponentAssociatedProject(component).getName());
						break;
					case PARAMETER:
						break;
					case TEMPLATE:
						resultQueryTemplates.put(component, jrmds.getComponentAssociatedProject(component).getName());
						break;
					default:
						break;
					}

				}
		 

		
		
		model.addAttribute("project",projectToBeDisplayed);
		
		model.addAttribute("numberOfResults", resultGroups.size() + resultConcepts.size() + resultConstraints.size() + resultQueryTemplates.size());
		model.addAttribute("numberOfGroups", resultGroups.size());
		model.addAttribute("numberOfConcepts", resultConcepts.size());
		model.addAttribute("numberOfConstraints", resultConstraints.size());
		model.addAttribute("numberOfTemplates", resultQueryTemplates.size());

		model.addAttribute("resultGroups", resultGroups);
		model.addAttribute("resultConcepts", resultConcepts);
		model.addAttribute("resultConstraints", resultConstraints);
		model.addAttribute("resultQueryTemplates", resultQueryTemplates);
		
		
		return "projectOverview";
	}
	
//ProjectProperties "INDEX"
	@RequestMapping(value = "/projectProps", method = RequestMethod.GET)
	public String showProperties(@RequestParam(required=true) String project, Model model) {
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		
		model.addAttribute("project" , p);
		return "projectProps";
	}

//SAVING NAME AND DESCRIPTION OF A PROJECT
	@RequestMapping(value ="/saveProps", method = RequestMethod.POST)
	public String saveProps(@RequestParam(required = true) String project, @RequestParam String name, @RequestParam String description, Model model){
		String msg ="";
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		
		//If new name == old name
		if((name.equals(p.getName()))) msg="The Project name didn't change!\n";
		//If new name != old name
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
				msg ="Project name successfully changed to " + name + ".";
			}
			else msg="The Project name already exists!";
		}
		
		
		//if description changed
		if(!description.equals(p.getDescription())){
			p.setDescription(description);
			msg = msg + "Description successfully changed.";
		}
		
		jrmds.saveProject(p);
		
		model.addAttribute("message",msg);
		model.addAttribute("linkRef","/projectProps?project="+p.getName());
		model.addAttribute("linkPro","/projectOverview?project="+p.getName());
		
		return "confirmation";
	}
	
	
	@RequestMapping(value ="/saveMembers", method = RequestMethod.POST)
	public String editMembers(@RequestParam(required = true) String  project){
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		
		return "redirect:projectProbs(project=${p.getName()})";
	}

//ADDING A EXTERNAL REPOSITIRY TO A PROJECT WITHOUT CHECK!
	@RequestMapping(value ="/addExternalRepos", method = RequestMethod.POST)
	public String addExternalRepos(@RequestParam(required = true) String  project, @RequestParam String externalrepo, Model model){
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		
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
	
//DELETING EXTERNAL REPOSITORIES FROM A PROJECT
	@RequestMapping(value ="/deleteExternalRepos", method = RequestMethod.POST)
	public String deleteExternalRepos(Model model, @RequestParam String  project, @RequestParam(required=false, defaultValue="", value = "isString") String[] isString){
		System.out.println(project);
		String msg = "";
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		
		if(isString.length >0) {
			for(int i=0; i<isString.length; i++){
				System.out.println(isString[i]);
				p.deleteExternalRepo(isString[i]);
			}
			jrmds.saveProject(p);
			msg = "The chosen External Repositories were removed from the Project.";
		}
		else msg = "No External Repositories were chosen to be removed.";
		
		model.addAttribute("message",msg);
		model.addAttribute("linkRef","/projectProps?project="+project);
		model.addAttribute("linkPro","/projectOverview?project="+project);
		
		return "confirmation";
	}
	
	
//DELETING PROJECT
	@RequestMapping(value ="/confirmDeleteProject", method = RequestMethod.POST)
	public String confirmDeleteProject(Model model, @RequestParam(required = true) String  project){
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		
		//Auszählen aller in Project befindlichen Components
		Set<Component> components = p.getComponents();
		Iterator<Component> iter = components.iterator();
		int constraints = 0;
		int templates = 0;
		int concepts = 0;
		int groups = 0;
		while (iter.hasNext()) {
			Component next = iter.next();
			if(next.getType() == ComponentType.CONSTRAINT){constraints++;}
			if(next.getType() == ComponentType.TEMPLATE){templates++;}
			if(next.getType() == ComponentType.GROUP){groups++;}
			if(next.getType() == ComponentType.CONCEPT){concepts++;}
			
		}
	
		model.addAttribute("project", p);
		model.addAttribute("constraintscount", constraints);
		model.addAttribute("templatescount", templates);
		model.addAttribute("groupscount", groups);
		model.addAttribute("conceptscount", concepts);
		
		
		return "confirmDeleteProject";
	}
	
	@RequestMapping(value ="/deleteProject", method = RequestMethod.POST)
	public String deleteProject(@RequestParam(required = true) String  project){
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		
		jrmds.deleteProject(p);
		return "redirect:projects";
	}
}
