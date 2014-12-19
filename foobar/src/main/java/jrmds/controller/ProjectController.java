package jrmds.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProjectController {
	@Autowired
	private JrmdsManagement jrmds;

	@Autowired
	private ViewController viewController;

	@Autowired
	private XmlController xmlController;
	
	
	private List<String> projectIndex;
	
	
/*
 ********************************************************************************************************* 
 *							GUEST
 ********************************************************************************************************* 
*/
	
	@RequestMapping(value = "/guestprojectProps", method = RequestMethod.GET)
	public String guestprojectProps(String project, Model model) {
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		
		model.addAttribute("project", p);
		return"guestprojectProps";
	}
	
	@RequestMapping(value = "/guestprojectOverview", method = { RequestMethod.POST, RequestMethod.GET })
	public String guestprojectOverview(@RequestParam(required = true) String project, Model model) {
		Project p = jrmds.getProject(project);
		if (p == null)
			throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		Map<Component, String> resultGroups = new HashMap<>();
		Map<Component, String> resultConcepts = new HashMap<>();
		Map<Component, String> resultConstraints = new HashMap<>();
		Map<Component, String> resultQueryTemplates = new HashMap<>();
		Project projectToBeDisplayed = jrmds.getProject(project);

		for (Component component : projectToBeDisplayed.getComponents()) {
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

		model.addAttribute("project", projectToBeDisplayed);

		model.addAttribute("numberOfResults", resultGroups.size() + resultConcepts.size() + resultConstraints.size() + resultQueryTemplates.size());
		model.addAttribute("numberOfGroups", resultGroups.size());
		model.addAttribute("numberOfConcepts", resultConcepts.size());
		model.addAttribute("numberOfConstraints", resultConstraints.size());
		model.addAttribute("numberOfTemplates", resultQueryTemplates.size());

		model.addAttribute("resultGroups", resultGroups);
		model.addAttribute("resultConcepts", resultConcepts);
		model.addAttribute("resultConstraints", resultConstraints);
		model.addAttribute("resultQueryTemplates", resultQueryTemplates);

		return "guestprojectOverview";
	}

		
/*
 ********************************************************************************************************* 
 *							USER
 ********************************************************************************************************* 
*/
	
	// CREATING A NEW PROJECT "INDEX"
	@RequestMapping(value = "/createNewProject", method = { RequestMethod.GET })
	public String createNewProject(Model model) {
		if(projectIndex == null) {
			projectIndex = new ArrayList<>();
			
		}
		
		return "createNewProject";
	}

	// ADDING A NEW PROJECT TO THE DATABASE
	@RequestMapping(value = "/addNewProject", method = RequestMethod.POST)
	public String addNewProject(Model model, String pName, String pDescription) {
		Project newProject;
		if(pDescription.equals("")){
			newProject = new Project(pName);
			}
		else {
			newProject = new Project(pName, pDescription);
		}
		jrmds.saveProject(newProject);
		return "redirect:projects";
	}

	// LIST OF ALL PROJECTS EXISTING
	@RequestMapping(value = "/projects", method = { RequestMethod.POST, RequestMethod.GET })
	public String projects(Model model) {
		Set<Project> projects = jrmds.getAllProjects();
		
		for(Project project : projects) {
			if(project.getDescription() == null) {
				project.setDescription("");
			}
		}
		
		model.addAttribute("projects", projects);
		return "projects";
	}

	// OVERVIEW OF ONE SELECTED PROJECTS
	// DISPLAYING PROJECTS CONTENT

	@RequestMapping(value = "/projectOverview", method = { RequestMethod.POST, RequestMethod.GET })
	public String projectOverview(@RequestParam(required = true) String project, Model model) {
		Project p = jrmds.getProject(project);
		if (p == null)
			throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		Map<Component, String> resultGroups = new HashMap<>();
		Map<Component, String> resultConcepts = new HashMap<>();
		Map<Component, String> resultConstraints = new HashMap<>();
		Map<Component, String> resultQueryTemplates = new HashMap<>();
		
		Set<String> tagCloud = new HashSet<>();
		
		Project projectToBeDisplayed = jrmds.getProject(project);
		
		if(projectToBeDisplayed.getDescription() == null) {
			projectToBeDisplayed.setDescription("");
		}
		
		boolean isSearchResult = false;

		for (Component component : projectToBeDisplayed.getComponents()) {
			tagCloud.addAll(component.getTags());
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

		model.addAttribute("project", projectToBeDisplayed);

		model.addAttribute("numberOfResults", resultGroups.size() + resultConcepts.size() + resultConstraints.size() + resultQueryTemplates.size());
		model.addAttribute("numberOfGroups", resultGroups.size());
		model.addAttribute("numberOfConcepts", resultConcepts.size());
		model.addAttribute("numberOfConstraints", resultConstraints.size());
		model.addAttribute("numberOfTemplates", resultQueryTemplates.size());

		model.addAttribute("isSearchResult", isSearchResult);
		
		model.addAttribute("tagCloud",tagCloud);
		
		model.addAttribute("resultGroups", resultGroups);
		model.addAttribute("resultConcepts", resultConcepts);
		model.addAttribute("resultConstraints", resultConstraints);
		model.addAttribute("resultQueryTemplates", resultQueryTemplates);

		return "projectOverview";
	}

	
	@RequestMapping(value= "/isProjectNameAvailable", method = {RequestMethod.POST,RequestMethod.GET })
	public @ResponseBody Boolean isProjectNameAvailable(@RequestParam(value = "pName", required = false) String desiredProjectName) {
		System.out.println(desiredProjectName);
		if(jrmds.getProject(desiredProjectName) == null) {
			return true;
		}
		else {
			System.out.println(jrmds.getProject(desiredProjectName).getDescription());
			return false;
		}
	}
	
	// ProjectProperties "INDEX"
	@RequestMapping(value = "/projectProps", method = RequestMethod.GET)
	public String showProperties(@RequestParam(required = true) String project, Model model) {
		Project p = jrmds.getProject(project);
		if (p == null)
			throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		model.addAttribute("project", p);
		return "projectProps";
	}

	// SAVING NAME AND DESCRIPTION OF A PROJECT
	@RequestMapping(value = "/saveProps", method = RequestMethod.POST)
	public String saveProps(@RequestParam(required = true) String project, @RequestParam String name, @RequestParam String description, Model model) {
		String msg = "";
		Project p = jrmds.getProject(project);
		if (p == null)
			throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		// If name changed
		if ((!name.equals(p.getName()))) {
			Set<Project> allprojects = jrmds.getAllProjects();
			Iterator<Project> iter = allprojects.iterator();

			// checking if name already exists in database
			while (iter.hasNext()) {
				Project next = iter.next();
				if (next.getName().equals(name))
					throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
			}
			p.setName(name);
			msg = "Project name successfully changed to " + name + ".";
		}

		// if description changed
		if (!description.equals(p.getDescription())) {
			p.setDescription(description);
			msg = msg + "Description successfully changed.";
		}

		jrmds.saveProject(p);

		model.addAttribute("message", msg);
		model.addAttribute("linkRef", "/projectProps?project=" + p.getName());
		model.addAttribute("linkPro", "/projectOverview?project=" + p.getName());

		return "confirmation";
	}

	@RequestMapping(value = "/saveMembers", method = RequestMethod.POST)
	public String editMembers(@RequestParam(required = true) String project, Model model) {
		Project p = jrmds.getProject(project);
		if (p == null)
			throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		model.addAttribute("project", p);
		return "redirect:projectProbs(project=${p.getName()})";
	}

	//BreadthsearchDUMMY for External Repos
	/*public void breadthSearch(Set<Component> cmpts, Map<String,Boolean> visit){
		Map<String,Boolean> visited = new HashMap<String, Boolean>();
		visited.putAll(visit);
		
		for(Component cp:cmpts){
			if(visited.get(cp.getRefID())) throw new IllegalArgumentException("The External Repository has a cycle!");
			else{
				Set<Component> referenced = new HashSet<Component>();
				referenced.addAll(cp.getReferencedComponents());
			}
		}	
	}*/
	
	//DEPTHSEARCH FOR EXTERNAL REPOS
	public void depthSearch(Component nr, Map<String, Boolean> visit){
		
		Map<String,Boolean> visited = new HashMap<String, Boolean>();
		visited.putAll(visit);
		
		//cycle, when node was visited before
		if(visited.get(nr.getRefID())) {
			throw new IllegalArgumentException("The External Repository has a cycle!");
		}
		else {
			//visited this node and setting it in the Map on true
			visited.replace(nr.getRefID(),true); 
			
			if((nr.getReferencedComponents()!=null)){
				if(nr.getReferencedComponents().size()>0){
					//getting all node references
					Set<Component> referenced = new HashSet<Component>();
					referenced.addAll(nr.getReferencedComponents());
				
					//checking for all references
						for(Component ref:referenced){
							this.depthSearch(ref,  visited);
						}
					}
			}
		
		
		}
	}
		

	// ADDING A EXTERNAL REPOSITIRY TO A PROJECT WITHOUT CHECK!
	@RequestMapping(value = "/addExternalRepos", method = RequestMethod.POST)
	public String addExternalRepos(@RequestParam(required = true) String project, @RequestParam String externalRepo, Model model) {
		Project p = jrmds.getProject(project);
		if (p == null)
			throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		// Checks if XML is valid
		if (!(xmlController.validateUrl(externalRepo)))
			throw new IllegalArgumentException("The External Repository is not a valid xml!");
		
		//gets the Set of Components out of the XML
		//Set<Component> newrepo = xmlController.	?

		Set<String> externalRepoList = p.getExternalRepos();

		// Check if external Repo already exists in the ExternalRepositorySet
		if (externalRepoList != null) {
			Iterator<String> iter = externalRepoList.iterator();
			while (iter.hasNext()) {
				String next = iter.next();
				if (next.equals(externalRepo))
					throw new IllegalArgumentException("The External Repository already exists!");
			}
		}
		
		//Check, if Cycles would be created
		/*
		Map<String, Boolean> visited = new HashMap<String, Boolean>();
		
		for(Component nr:newRepo){
			visited.put(nr.getRefID(), false);
		}
		
		for(Component nr:newRepo){
		System.out.println("STEP");
		pc.depthSearch(nr, visited);
		}
		*/
		
		//*Check if some ID is identical to the intern Repo
		Boolean duplicate = false;
		Set<Component> bothSets = new HashSet<Component>();
			 /* 
			 bothsets.addAll(jrmds.getIntersection(newrepo, externalrepo, false);	
			 if(bothsets.size()>0) Boolean duplicate = true;
			*/
		
	
		p.addExternalRepo(externalRepo);
		jrmds.saveProject(p);
		
		String msg ="New Repository successfully added!";
	
		if(duplicate){
			msg = msg + " Following Component IDs are identical to IDs of the existing External Repository: ";
			for(Component component : bothSets){
				msg = msg + component.getRefID() + " " ;
			}
			msg = msg + " Exporting the Components to an XML File, the internal Components will be overwritten by the external Ones with same ID.";
		}
		
		model.addAttribute("message",msg);
		model.addAttribute("linkRef", "/projectProps?project=" + p.getName());
		model.addAttribute("linkPro", "/projectOverview?project=" + p.getName());
		return "confirmation";
	} 

	// DELETING EXTERNAL REPOSITORIES FROM A PROJECT
	@RequestMapping(value = "/deleteExternalRepos", method = RequestMethod.POST)
	public String deleteExternalRepos(Model model, @RequestParam String project,
			@RequestParam(required = false, defaultValue = "", value = "isString") String[] isString) {
		Project p = jrmds.getProject(project);
		if (p == null)
			throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		String msg = "";

		if (isString.length > 0) {
			for (int i = 0; i < isString.length; i++) {
				System.out.println(isString[i]);
				p.deleteExternalRepo(isString[i]);
			}

			jrmds.saveProject(p);
			msg = "The chosen External Repositories were removed from the Project.";
		} else
			msg = "No External Repositories were chosen to be removed.";

		model.addAttribute("message", msg);
		model.addAttribute("linkRef", "/projectProps?project=" + project);
		model.addAttribute("linkPro", "/projectOverview?project=" + project);

		return "confirmation";
	}

	// CONFIRMING DELETION OF PROJECT
	@RequestMapping(value = "/confirmDeleteProject", method = RequestMethod.GET)
	public String confirmDeleteProject(Model model, @RequestParam(required = true) String project) {
		Project p = jrmds.getProject(project);
		if (p == null)
			throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		// Auszählen aller in Project befindlichen Components
		Set<Component> components = p.getComponents();
		Iterator<Component> iter = components.iterator();
		int constraints = 0;
		int templates = 0;
		int concepts = 0;
		int groups = 0;

		while (iter.hasNext()) {
			Component next = iter.next();
			if (next.getType() == ComponentType.CONSTRAINT) {
				constraints++;
			}
			if (next.getType() == ComponentType.TEMPLATE) {
				templates++;
			}
			if (next.getType() == ComponentType.GROUP) {
				groups++;
			}
			if (next.getType() == ComponentType.CONCEPT) {
				concepts++;
			}
		}

		model.addAttribute("project", p);
		model.addAttribute("constraintscount", constraints);
		model.addAttribute("templatescount", templates);
		model.addAttribute("groupscount", groups);
		model.addAttribute("conceptscount", concepts);

		return "confirmDeleteProject";
	}

	// DELETE PROJECT
	@RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
	public String deleteProject(@RequestParam(required = true) String project) {
		Project p = jrmds.getProject(project);
		if (p == null)
			throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		jrmds.deleteProject(p);

		return "redirect:projects";
	}
}
