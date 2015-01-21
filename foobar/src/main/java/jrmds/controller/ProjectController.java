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

import javax.servlet.http.HttpServletRequest;

import jrmds.main.JrmdsManagement;
import jrmds.model.Component;
import jrmds.model.ComponentType;
import jrmds.model.Constraint;
import jrmds.model.EnumConflictCause;
import jrmds.model.*;
import jrmds.model.ImportItem;
import jrmds.model.ImportReferenceError;
import jrmds.model.ImportResult;
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
import org.springframework.web.multipart.MultipartFile;
/** This class handles all methods and REST calls for projects. */
@Controller
public class ProjectController {
	/** An instance of the JrmdsManagement class to handle the database communication. */
	@Autowired
	private JrmdsManagement jrmds;
	/** An instance of the UserManagement class to handle users */ 
	@Autowired
	private UserManagement userManagment;
	/** An instance of the ViewController class to handle the users.*/
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
	 * @param regUser  The user status.
	 * @return createNewProject  The html document to create a new Project.
	 * @throws IllegalArgumentException if the user has no right to do this.
	 */
	@RequestMapping(value = "/createNewProject", method = { RequestMethod.GET })
	public String createNewProject(
			@CurrentUser RegistredUser regUser,
			Model model) {
		if (regUser==null) throw new IllegalArgumentException("You are not allowed to do this!");
		if(projectIndex == null) {
			projectIndex = new ArrayList<>();
		}
		return "createNewProject";
	}

	/**
	 * Rest call to add the project to the database.
	 * @param regUser 		The user status.
	 * @param model
	 * @param pName  		name of the project.
	 * @param pDescription  description of the project.
	 * @return redirect:projects  Redirects to the html document with the list of all projects.
	 * @throws IllegalArgumentException if a project with pname already exists or the user has no right to do this.
	 */
	@RequestMapping(value = "/addNewProject", method = { RequestMethod.GET, RequestMethod.POST })
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
		if (regUser==null) throw new IllegalArgumentException("You are not allowed to do this!");
		
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
	 * @throws IllegalArgumentException If a project p doesn't exist.
	 */
	@RequestMapping(value = "/projectOverview", method = { RequestMethod.POST, RequestMethod.GET })
	public String projectOverview(
			@RequestParam(required = true) String project,
			Model model) {
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
				resultGroups.put(component, project);
				break;
			case CONCEPT:
				resultConcepts.put(component, project);
				break;
			case CONSTRAINT:
				resultConstraints.put(component, project);
				break;
			case PARAMETER:
				break;
			case TEMPLATE:
				resultQueryTemplates.put(component, project);
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
	 * Rest call to check if the desired project name is available.
	 * @param desiredProjectName  
	 * @param regUser  The user status.
	 * @return true or false  depending on, if the project name already exists in the database
	 * @throws IllegalArgumentException if the user has no right to do this.
	 */
	@RequestMapping(value= "/isProjectNameAvailable", method = {RequestMethod.POST,RequestMethod.GET })
	public @ResponseBody Boolean isProjectNameAvailable(
			@CurrentUser RegistredUser regUser,
			@RequestParam(value = "pName",required = false) String desiredProjectName) {
		if (regUser==null) throw new IllegalArgumentException("You are not allowed to do this!");
		if(jrmds.getProject(desiredProjectName) == null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Rest call to get an overview of the properties of a project.
	 * @param project 	name of the project.
	 * @param delUser 	deletion of an user.
	 * @param regUser	The user status.
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

	/**
	 * Rest call to save name and description of a Project in the database.
	 * @param project 		The name of the current project.
	 * @param name			The new name for the project.
	 * @param description	The new description for the project.
	 * @param model
	 * @param regUser		The user status.
	 * @return confirmation 	The html document to confirm the successful change.
	 * @throws IllegalArgumentException if the project doesn't exist or the user has no right to do this.
	 */
	@RequestMapping(value = "/saveProps", method = { RequestMethod.GET, RequestMethod.POST })
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
		if (regUser==null || !userManagment.workingOn(regUser, p)) throw new IllegalArgumentException("You are not allowed to do this!");

		/**
		 * If the name changed, check, if a project with that name already exists in the database.
		 * @throws IllegalArgumentException if the new project name is already taken.
		 */
		if ((!name.equals(p.getName()))) {
			Set<Project> allprojects = jrmds.getAllProjects();
			Iterator<Project> iter = allprojects.iterator();

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

	/**
	 * Rest call to save Members in the project,
	 * @param project  	The project name.
	 * @param newMember The new member which is to be added.
	 * @param regUser	The user status
	 * @param model
	 * @return projectProps html document that shows projects properties.
	 * @throws IllegalArgumentException if the project doesn't exist or the user has no right to do this or newMember doesn't exist.
	 */
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
	
	/**
	 * DepthSearch to find cycles in the references.
	 * @param nr	The current Component which references are to check.
	 * @param visit	The Map of already visited Components.
	 * @throws IllegalArgumentException if a cycle is found.
	 * @throws NullPointerException if nr is null.
	 */
	public void depthSearch(Component nr, Map<String, Boolean> visit){
		if (nr == null) throw new NullPointerException("One referenced Component is null! The XML is not complete.");
		Map<String,Boolean> visited = new HashMap<String, Boolean>();
		visited.putAll(visit);
		
		/**
		 * When a node was visited before: A cycle is found.
		 */
		if(visited.get(nr.getRefID())) {
			throw new IllegalArgumentException("The External Repository has a cycle!");
		}
		else {
			/**
			 * The current node now was visited. Thus its value in the visited Map is true.
			 */
			visited.replace(nr.getRefID(),true); 
			
			if((nr.getReferencedComponents()!=null)){
				if(nr.getReferencedComponents().size()>0){
					/**
					 * Gets all references of the current node.
					 */
					Set<Component> referenced = new HashSet<Component>();
					referenced.addAll(nr.getReferencedComponents());
				
					/**
					 * Checks for all those references for cycles.
					 */
						for(Component ref:referenced){
							this.depthSearch(ref,  visited);
						}
					}
			}
		}
	}
		

	/**
	 * Rest call to add an external Repository to a project or checks for intersections.
	 * @param project  		The name of the current project.
	 * @param externalRepo	The external Repository which is to be added.
	 * @param type			To check if a repository is to be added to a group or to a project.
	 * @param RefID			The RefID of the group, if type=GROUP.
	 * @param regUser		The user status.
	 * @param add			To check, if a new external Repository is to be added, or if the function is used to check for intersections.
	 * @param model
	 * @return	confirmationIntersection The html document which shows all current intersections and confirms the added Repositories.
	 * @throws InvalidObjectException
	 * @throws MalformedURLException
	 * @throws XmlParseException
	 * @throws IllegalArgumentException if the project/group doesn't exist or the user has no right to do this.
	 */
	@RequestMapping(value = "/addExternalRepos",  method = {RequestMethod.POST,RequestMethod.GET })
	public String addExternalRepos(
			@RequestParam(required = true) String project, 
			@RequestParam String externalRepo,
			@RequestParam(defaultValue="PROJECT") String type,
			@RequestParam(defaultValue="") String RefID,
			@CurrentUser RegistredUser regUser,
			@RequestParam Boolean add,
			Model model
			) throws InvalidObjectException, MalformedURLException, XmlParseException {	
			Project p = jrmds.getProject(project);
			Group g = null;
			Set<String> externalRepoList;
			if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
			if (regUser==null || !userManagment.workingOn(regUser, p)) throw new IllegalArgumentException("You are not allowed to do this!");
			
			
			if (type.equals("GROUP"))	{
				g = jrmds.getGroup(p, RefID);
				if (g == null) throw new IllegalArgumentException("Group-RefID invalid");
			}
			
			String msg ="";
			
			/**
			 * If a new external Repository is to be added to the project.
			 * Checks if the external Repository is a valid XML
			 */
			if(add) _logic.validateExternalRepositoryAndThrowException(externalRepo);
				
			/** Gets a Set of Components out of the external Repository. */
			Set<Component> newRepo = new HashSet<Component>();
			for (ImportItem item : _logic.XmlToObjectsFromUrl(externalRepo).iterateImportItems())
				newRepo.add(item.getComponent());
				
			/**
			 * If a new external Repository is to be added to the project.
			 * Check if external Repository already exists in the ExternalRepositorySet
			 */
			if(add) {
				if (type.equals("GROUP")) externalRepoList = new HashSet<String>(); 
				else externalRepoList = new HashSet<String>(p.getExternalRepos());
				
				if (externalRepoList != null) {
					Iterator<String> iter = externalRepoList.iterator();
					while (iter.hasNext()) {
						String next = iter.next();
						if (next.equals(externalRepo)) throw new IllegalArgumentException("The External Repository already exists!");
					}
				}
				
				/**
				 * Check, if Cycles would be created
				 */
				Map<String, Boolean> visited = new HashMap<String, Boolean>();
				
				for(Component nr:newRepo){
					visited.put(nr.getRefID(), false);
				}
				
				for(Component nr:newRepo){
					this.depthSearch(nr, visited);
				}
			}
			
			/**
			 * Check if some ID is identical to the intern Repository(checks for intersections)
			 */
			Set<Component> bothSets = new HashSet<Component>();
			
			bothSets.addAll(jrmds.getIntersection(p.getComponents(), newRepo,  false));	
			if(add && bothSets.size()==0) msg ="New Repository successfully added!";
					
			if (type.equals("GROUP")) {
				g.addExternalRepo(externalRepo);
				jrmds.saveComponent(p, g);
				model.addAttribute("linkRef", "/editGroup?project=" + p.getName() + "&group=" + g.getRefID());
			} else {
				p.addExternalRepo(externalRepo);
				jrmds.saveProject(p);
				model.addAttribute("linkRef", "/projectProps?project=" + p.getName());
			}
		
			model.addAttribute("project",p);	
			model.addAttribute("bothSets", bothSets);
			model.addAttribute("linkPro", "/projectOverview?project=" + p.getName());
			model.addAttribute("message",msg);
			
			return "confirmationIntersections";
	} 

	/**
	 * Rest call to delete an external Repository from the project/group.
	 * @param model
	 * @param regUser	The user status.
	 * @param project	The name of the current project.
	 * @param type		If the external Repository is to be deleted from a project or from a group.
	 * @param RefID		The RefID if it is a group.
	 * @param isString
	 * @return	confirmation 	The html document which confirms the deletion of the external Repository.
	 * @throws IllegalArgumentException if the project/group doesn't exist or the user has no right to do this.
	 */
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
		if (regUser==null || !userManagment.workingOn(regUser, p)) throw new IllegalArgumentException("You are not allowed to do this!");

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
	
	 /** Rest call to import a local Xml File into the project.
	 * @param model
	 * @param regUser	The user status.
	 * @param project	The name of the current project.
	 * @param type		If the external Repository is to be deleted from a project or from a group.
	 * @param RefID		The RefID if it is a group.
	 * @param isString
	 * @return	confirmation 	The html document which confirms the deletion of the external Repository.
	 * @throws IllegalArgumentException if the project/group doesn't exist or the user has no right to do this.
	 * @throws IllegalArgumentException if XML file is empty.
	 */
	@RequestMapping(value = "/importXmlFile", method = RequestMethod.POST)
	public String importXml(
			Model model,
			HttpServletRequest request,
			@CurrentUser RegistredUser regUser,
			@RequestParam("project") String projectName,
			@RequestParam("file") MultipartFile file,
			@RequestParam(defaultValue="PROJECT") String type,
			@RequestParam(defaultValue="") String RefID) throws Exception
	{
		if (file.isEmpty()) throw new IllegalArgumentException("The XML File is empty!");
		Project targetProject = jrmds.getProject(projectName);
		if (targetProject == null) 
			throw new IllegalArgumentException("Project-name " + projectName + " invalid, Project not existent");
		if (regUser==null || !userManagment.workingOn(regUser, targetProject)) 
			throw new IllegalArgumentException("You are not allowed to do this!");
		
		byte[] bytes = file.getBytes();
		String xmlContent = new String(bytes, "UTF-8"); 
		
		/**
		 * Gets the Components out of the XML file into an ImportResult object.
		 * Then checks for duplicates.
		 */
		ImportResult xmlResult = _logic.XmlToObjectsFromString(xmlContent);
		xmlResult = _logic.analyseForDoubleItems(targetProject, xmlResult);
		    
		/** Saving the xmlResult in a windowsession for later usage in /saveImportXmlFile */
		request.getSession().setAttribute("xmlImport_" + projectName, xmlResult);
		
		/** List with all imported components */
		List<ImportItem> importList = new ArrayList<ImportItem>();
		
		/** List with reference conflicted components */
		List<ImportReferenceError> refErrList = new ArrayList<ImportReferenceError>();
		
		for(ImportItem imp: xmlResult.iterateImportItems()){
			if(imp.getCause() != EnumConflictCause.None) importList.add(imp);
		}
		
		for (ImportReferenceError refErr : xmlResult.iterateImportReferenceError())
       		refErrList.add(refErr);

		/**
		 * If no reference Errors or duplicate Errors were found, save the Components in the project.
		 * Saving may take a while with long Component list.
		 */
		if(importList.size() == 0 && xmlResult.getImportReferenceErrorSize() == 0)
       		for(ImportItem c : xmlResult.iterateImportItems())
       			jrmds.saveComponent(targetProject, c.getComponent());
       	
       	model.addAttribute("linkRef", "/projectProps?project=" + targetProject.getName());
		model.addAttribute("linkPro", "/projectOverview?project=" + targetProject.getName());

        model.addAttribute("importList", importList);
		model.addAttribute("refErrList", refErrList);
		model.addAttribute("project", targetProject);
		
		return "confirmationImport";
	}
	
	/**
	 * Rest call so save the Components of an XMLFile Import after errors and selection into the project.
	 * @param model
	 * @param request
	 * @param regUser		The user status.
	 * @param projectName	Name of the current project.
	 * @param type			Type of the target location
	 * @param RefID			refID if not project as target location
	 * @param isChecked		collection of selected components with errors
	 * @return	confirmationImport	html document to confirm import
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveImportXmlFile", method = RequestMethod.POST)
		public String saveImportXml(
				Model model,
				HttpServletRequest request,
				@CurrentUser RegistredUser regUser,
				@RequestParam("project") String projectName,
				@RequestParam(defaultValue="PROJECT") String type,
				@RequestParam(defaultValue="") String RefID,
				@RequestParam (defaultValue = "") String[] isChecked) throws Exception
		{
			
			Project targetProject = jrmds.getProject(projectName);
			if (targetProject == null) 
				throw new IllegalArgumentException("Project-name " + projectName + " invalid, Project not existent");
			if (regUser==null || !userManagment.workingOn(regUser, targetProject)) 
				throw new IllegalArgumentException("You are not allowed to do this!");
			
			/** Getting the in windowsession saved variable to work with it */
			ImportResult xmlResult = (ImportResult)request.getSession().getAttribute("xmlImport_" + projectName);
			
	      	/** List of Components which is later to be saved in the project */
	      	Set<Component> toAdd = new HashSet<Component>();
	      	
	      	
	      	/** 
	      	 * Saving components with no cause, if they don't have reference errors 
	      	 */
	      	for(ImportItem imp : xmlResult.iterateImportItems()){
	      		Boolean found = false;
	      		if(imp.getCause()==EnumConflictCause.None){
			      	for(ImportReferenceError ref :xmlResult.iterateImportReferenceError()){
			      		System.out.println(imp.getComponent().getRefID() + ref.getItemId());
			      		System.out.println(imp.getComponent().getType().toString() + ref.getItemType().toString());
			      		if(imp.getComponent().getRefID().equals(ref.getItemId()) && imp.getComponent().getType()==ref.getItemType()) {
			      			System.out.println("true");
			      			found = true; 
			      			break;
			      		}
			      	}
			      		if(!found) { System.out.println("Adding1:"  + imp.getComponent().getRefID()); toAdd.add(imp.getComponent());}
	      		}
		      }
	      	
	      		
	      	/**
	      	 * Saving the Components to the project if they match the chosen Ones in isChecked
	      	 */
	      	for(String check : isChecked){
	      		/**test if check is an database error */
	      		for(ImportItem imp: xmlResult.iterateImportItems()){
	      			String compare = "item" + imp.getComponent().getRefID().hashCode();
	      			if(compare.equals(check)) { System.out.println("Adding2:"  + imp.getComponent().getRefID()); toAdd.add(imp.getComponent());}
	      		}
	      	
	      		/** test if check is an reference error and if so, get the component from the importList*/
	      		for(ImportReferenceError ref : xmlResult.iterateImportReferenceError()){
	      			String compare = "ref" + ref.getItemId().hashCode() + ref.getItemType().toString().hashCode();
	      			if(compare.equals(check)) {
	      				for(ImportItem imp: xmlResult.iterateImportItems()){
	      					if(imp.getComponent().getRefID().equals(ref.getItemId()) && imp.getComponent().getType()==ref.getItemType()) { System.out.println("Adding3:"  + imp.getComponent().getRefID()); toAdd.add(imp.getComponent());}
	      				}
	      			}
	      		}
	      	}
	      		
	      	/**
	      	 * Finally saving the Components to the project.
	      	 */
	      	for(Component c : toAdd){
	      		
	      		Component compare = null;
	      		switch(c.getType()){
	      		case GROUP: compare = jrmds.getComponent(targetProject, new Group(c.getRefID()));break;
	      		case CONSTRAINT: compare = jrmds.getComponent(targetProject, new Constraint(c.getRefID()));break;
	      		case CONCEPT: compare = jrmds.getComponent(targetProject, new Concept(c.getRefID()));break;
	      		case TEMPLATE:compare = jrmds.getComponent(targetProject, new QueryTemplate(c.getRefID()));break;
	      		default: throw new IllegalArgumentException("Component-type not specified");
	      		}
	      		
	      		if(compare!=null) {System.out.println("FAILURE INCOMING!");jrmds.deleteComponent(targetProject, compare);}
	      		jrmds.saveComponent(targetProject, c);
	      		 
	      	}
	 
	    	model.addAttribute("linkRef", "/projectProps?project=" + targetProject.getName());
			model.addAttribute("linkPro", "/projectOverview?project=" + targetProject.getName());
			
	       	model.addAttribute("importList", new ArrayList<ImportItem>());
	       	model.addAttribute("refErrList", new ArrayList<ImportReferenceError>());
			model.addAttribute("project", targetProject);
			return "confirmationImport";
		}

	
	/**
	 * Rest call to get a html document to confirm the deletion of a project.
	 * @param model
	 * @param project  the name of the project.
	 * @param regUser  The user status.
	 * @return confirmDeleteProject  html document to confirm the deletion of a project
	 * @throws IllegalArgumentException if the project/group doesn't exist or the user has no right to do this.
	 */
	@RequestMapping(value = "/confirmDeleteProject", method = RequestMethod.GET)
	public String confirmDeleteProject(
			@CurrentUser RegistredUser regUser,
			Model model, 
			@RequestParam(required = true) String project
			) {
		Project p = jrmds.getProject(project);
		if (p == null)	throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser==null || !userManagment.workingOn(regUser, p)) throw new IllegalArgumentException("You are not allowed to do this!");

		/**
		 * Counts the amount of each Component type in the project.
		 */
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

	/**
	 * Rest Call to delete a project.
	 * @param regUser  The user status.
	 * @param project  The name of the current project.
	 * @return redirect:projects  A redirection to the html document projects.
	 * @throws IllegalArgumentException if the project/group doesn't exist or the user has no right to do this.
	 */
	@RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
	public String deleteProject(
			@CurrentUser RegistredUser regUser,
			@RequestParam(required = true) String project
			) {
		Project p = jrmds.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser==null || !userManagment.workingOn(regUser, p)) throw new IllegalArgumentException("You are not allowed to do this!");

		jrmds.deleteProject(p);

		return "redirect:projects";
	}
	
}
