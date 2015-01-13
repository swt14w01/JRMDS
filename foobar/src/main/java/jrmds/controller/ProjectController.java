
package jrmds.controller;

import java.io.InvalidObjectException;
import java.net.MalformedURLException;
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
import jrmds.model.Group;
import jrmds.model.Project;
import jrmds.model.RegistredUser;
import jrmds.security.CurrentUser;
import jrmds.user.UserManagement;
import jrmds.xml.XmlLogic;
import jrmds.xml.XmlParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/** This class handles all methods and REST calls for projects. */
@Controller
public class ProjectController {
	/** An instance of the JrmdsManagement class to handle the database communication. */
	@Autowired
	private JrmdsManagement jrmds;
	/** An instance of the UserManagement class to handle users */ 
	@Autowired
	private UserManagement userManagment;
	/** An instance of the ViewController class to handle the ..*/
	@Autowired
	private ViewController viewController;
	/** An instance of the XmlLogic class to handle usage of xml*/
	@Autowired
	private XmlLogic _logic;
	
	/** List of the Names of all existing projects. */
	private List<String> projectIndex;

	
	
	/**
	 * REST call to get the html document createNewProject.
	 * @param model
	 * @return createNewProject  The html document to create a new Project.
	 */
	@RequestMapping(value = "/createNewProject", method = { RequestMethod.GET })
	public String createNewProject(Model model) {
		if(projectIndex == null) {
			projectIndex = new ArrayList<>();
		}
		return "createNewProject";
	}

	/**
	 * Rest call to add the project to the database.
	 * @param regUser  current user
	 * @param model
	 * @param pName   name of the project.
	 * @param pDescription  description of the project.
	 * @return redirect:projects  Redirects to the html document with the list of all projects.
	 * @throws IllegalArgumentException if a project with pname already exists.
	 */
	@RequestMapping(value = "/addNewProject", method = RequestMethod.POST)
	public String addNewProject(
			@CurrentUser RegistredUser regUser,
			Model model, 
			String pName, 
			String pDescription
			) {
		/**
		 * Checks if a project with name pName already exists in the database.
		 */
		Project p = jrmds.getProject(pName);
		if (p != null) throw new IllegalArgumentException("Project-name " + pName + " invalid, Project already exists!");
		if (regUser==null) throw new IllegalArgumentException("Only registered users can create a new project");
		
		Project newProject;
		if(pDescription.equals("")){
			newProject = new Project(pName);
			}
		else {
			newProject = new Project(pName, pDescription);
		}
		newProject = jrmds.saveProject(newProject);
		userManagment.userWorksOn(regUser, newProject);
		return "redirect:projects";
	}

	/**
	 * Rest call to get the list of all projects.
	 * @param model
	 * @return projects  The html document with a list of all existing projects,
	 */
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

	/**
	 * Rest call to get an overview of the project with its components.
	 * @param project   name of the project.
	 * @param model
	 * @return projectOverview  The html document which shows all project properties and functions depending on user status.
	 */
	@RequestMapping(value = "/projectOverview", method = { RequestMethod.POST, RequestMethod.GET })
	public String projectOverview(@RequestParam(required = true) String project, Model model) {
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		Map<Component, String> resultGroups = new HashMap<>();
		Map<Component, String> resultConcepts = new HashMap<>();
		Map<Component, String> resultConstraints = new HashMap<>();
		Map<Component, String> resultQueryTemplates = new HashMap<>();
		
		Set<String> tagCloud = new HashSet<>();
		
		Project projectToBeDisplayed = jrmds.getProject(project);
		/** 
		 * If a project description is null, show "" instead 
		 */
		if(projectToBeDisplayed.getDescription() == null) {
			projectToBeDisplayed.setDescription("");
		}
		
		boolean isSearchResult = false;
		/**
		 * Getting the Tags for all the Components in the project.
		 */
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

	/**
	 * Checks if the desired project name is available.
	 * @param desiredProjectName  
	 * @return true or false  depending on, if the project name already exists in the database
	 */
	@RequestMapping(value= "/isProjectNameAvailable", method = {RequestMethod.POST,RequestMethod.GET })
	public @ResponseBody Boolean isProjectNameAvailable(@RequestParam(value = "pName", required = false) String desiredProjectName) {
		if(jrmds.getProject(desiredProjectName) == null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Rest call to get an overview of the properties of a project.
	 * @param project  name of the project.
	 * @param delUser  deletion of an user.
	 * @param regUser
	 * @param model
	 * @return projectProps or guestprojectProps  html document showing the properties of the project. If user is logged in, with functions to edit.
	 * @throws IllegalArgumentException if the project doesn't exist.
	 */
	@RequestMapping(value = "/projectProps", method = RequestMethod.GET)
	public String showProperties(
			@RequestParam(required = true) String project,
			@RequestParam(defaultValue = "") String delUser,
			@CurrentUser RegistredUser regUser,
			Model model
			
			) {
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		/** 
		 * If deletion was selected.. the current user can not delete himself
		 */
		if (delUser.length()>0 && regUser!=null && !regUser.getName().equals(delUser) && userManagment.workingOn(regUser, p)) {
			RegistredUser r = userManagment.getUser(delUser);
			userManagment.userNotWorksOn(r, p);
		}
		
		model.addAttribute("project", p);
		model.addAttribute("users", jrmds.getProjectUsers(p));
		
		if (userManagment.workingOn(regUser, p)) {
			return "projectProps";
		} else {
			return "guestprojectProps";
		}
	}

	// SAVING NAME AND DESCRIPTION OF A PROJECT
	@RequestMapping(value = "/saveProps", method = RequestMethod.POST)
	public String saveProps(
			@RequestParam(required = true) String project, 
			@RequestParam String name, 
			@RequestParam String description, 
			Model model,
			@CurrentUser RegistredUser regUser
			) {
		
		String msg = "";
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		
		if (regUser==null || !userManagment.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");

		// If name changed
		if ((!name.equals(p.getName()))) {
			Set<Project> allprojects = jrmds.getAllProjects();
			Iterator<Project> iter = allprojects.iterator();

			// checking if name already exists in database
			while (iter.hasNext()) {
				Project next = iter.next();
				if (next.getName().equals(name))
					throw new IllegalArgumentException("Project-name " + project + " invalid, Project already exists!");
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
	public String editMembers(
			@RequestParam(required = true) String project,
			@RequestParam String newMember,
			@CurrentUser RegistredUser regUser,
			Model model
			) {
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		
		RegistredUser r = userManagment.getUser(newMember);
		if (r == null) throw new IllegalArgumentException("User " + newMember + " not existent.");
		
		if (regUser!=null && userManagment.workingOn(regUser, p)) userManagment.userWorksOn(r, p);
		
		model.addAttribute("project", p);
		model.addAttribute("users", jrmds.getProjectUsers(p));
		return "projectProps";
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
		if (nr == null) throw new NullPointerException("One referenced Component is null! The XML is not complete.");
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
	public String addExternalRepos(
			@RequestParam(required = true) String project, 
			@RequestParam String externalRepo,
			@RequestParam(defaultValue="PROJECT") String type,
			@RequestParam(defaultValue="") String RefID,
			@CurrentUser RegistredUser regUser,
			Model model
			) throws InvalidObjectException, MalformedURLException, XmlParseException {	
		
		Project p = jrmds.getProject(project);
		Group g = null;
		Set<String> externalRepoList;
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (type.equals("GROUP")) {
			g = jrmds.getGroup(p, RefID);
			if (g == null) throw new IllegalArgumentException("Group-RefID invalid");
			externalRepoList = new HashSet<String>(); //fuck off incompetent Spring-Data°°!°!!!!!!!!!!!!!!11111
		} else {
			externalRepoList = new HashSet<String>(p.getExternalRepos());
		}
		if (regUser==null || !userManagment.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		
		// Checks if XML is valid
		_logic.validateExternalRepositoryAndThrowException(externalRepo);
		
		//gets the Set of Components out of the XML
		Set<Component> newRepo = _logic.XmlToObjectsFromUrl(externalRepo);

		// Check if external Repo already exists in the ExternalRepositorySet
		if (externalRepoList != null) {
			Iterator<String> iter = externalRepoList.iterator();
			while (iter.hasNext()) {
				String next = iter.next();
				if (next.equals(externalRepo)) throw new IllegalArgumentException("The External Repository already exists!");
			}
		}
		
		//Check, if Cycles would be created
		
		Map<String, Boolean> visited = new HashMap<String, Boolean>();
		
		for(Component nr:newRepo){
			visited.put(nr.getRefID(), false);
		}
		
		for(Component nr:newRepo){
			this.depthSearch(nr, visited);
		}
		
		
		//*Check if some ID is identical to the intern Repo
		Boolean duplicate = false;
		Set<Component> bothSets = new HashSet<Component>();
			 
		bothSets.addAll(jrmds.getIntersection(p.getComponents(), newRepo,  false));	
		if(bothSets.size()>0)  duplicate = true;
	
		if (type.equals("GROUP")) {
			g.addExternalRepo(externalRepo);
			jrmds.saveComponent(p, g);
			model.addAttribute("linkRef", "/editGroup?project=" + p.getName() + "&group=" + g.getRefID());
		} else {
			p.addExternalRepo(externalRepo);
			jrmds.saveProject(p);
			model.addAttribute("linkRef", "/projectProps?project=" + p.getName());
		}
		
		String msg ="New Repository successfully added!";
	
		if(duplicate){
			msg = msg + " Following Component IDs are identical to IDs of the existing External Repository: ";
			for(Component component : bothSets){
				msg = msg + component.getRefID() + " " ;
			}
			msg = msg + " Exporting the Components to an XML File, the internal Components will be overwritten by the external Ones with same ID.";
		}
		
		model.addAttribute("linkPro", "/projectOverview?project=" + p.getName());
		model.addAttribute("message",msg);
		return "confirmation";
	} 

	// DELETING EXTERNAL REPOSITORIES FROM A PROJECT
	@RequestMapping(value = "/deleteExternalRepos", method = RequestMethod.POST)
	public String deleteExternalRepos(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam(defaultValue="PROJECT") String type,
			@RequestParam(defaultValue="") String RefID,
			@RequestParam(required = false, defaultValue = "", value = "isString") String[] isString) {
		
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser==null || !userManagment.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");

		String msg = "";
		String linkRef = "";
		
		Group g = null;
		if (isString.length > 0) {
			if (type.equals("GROUP")) {
				g = jrmds.getGroup(p, RefID);
				if (g == null) throw new IllegalArgumentException("Group-RefID invalid");
				for (int i = 0; i < isString.length; i++) g.deleteExternalRepo(isString[i]);
				jrmds.saveComponent(p,g);
				msg = "The chosen External Repositories were removed from the Group.";
				linkRef = "/editGroup?project=" + p.getName() + "&group=" + g.getRefID();
			} else {
				for (int i = 0; i < isString.length; i++) p.deleteExternalRepo(isString[i]);
				jrmds.saveProject(p);
				msg = "The chosen External Repositories were removed from the Project.";
				linkRef = "/projectProps?project=" + project;
			}
		} else { 
			if (type.equals("GROUP")) {
				g = jrmds.getGroup(p, RefID);
				if (g == null) throw new IllegalArgumentException("Group-RefID invalid");
				linkRef = "/editGroup?project=" + p.getName() + "&group=" + g.getRefID();
			} else {
				linkRef = "/projectProps?project=" + project;
			}
			msg = "No External Repositories were chosen to be removed.";
		}	
		
		model.addAttribute("linkRef", linkRef);
		model.addAttribute("message", msg);
		model.addAttribute("linkPro", "/projectOverview?project=" + project);

		return "confirmation";
	}

	// CONFIRMING DELETION OF PROJECT
	@RequestMapping(value = "/confirmDeleteProject", method = RequestMethod.GET)
	public String confirmDeleteProject(
			Model model, 
			@RequestParam(required = true) String project
			) {
		
		
		Project p = jrmds.getProject(project);
		if (p == null)	throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

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
	public String deleteProject(
			@RequestParam(required = true) String project
			) {
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		jrmds.deleteProject(p);

		return "redirect:projects";
	}
}
