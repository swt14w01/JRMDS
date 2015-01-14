package jrmds.main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jrmds.model.Component;
import jrmds.model.Concept;
import jrmds.model.Constraint;
import jrmds.model.Group;
import jrmds.model.Parameter;
import jrmds.model.Project;
import jrmds.model.QueryTemplate;
import jrmds.model.RegistredUser;
import jrmds.xml.XmlLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class JrmdsManagement {
	@Autowired
	private RuleRepository ruleRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ParameterRepository parameterRepository;
	@Autowired
	private XmlLogic _logic;

	/*************************************************************************
	 ************************* GETTERS*****************************************
	 *************************************************************************/

	/**
	 * Search a Project by its name
	 * @param projectName
	 * @return the corresponding project from the database or NULL if error
	 * @throws IndexOutOfBoundsException if due to an error two projects with same name exist in the database (should be impossible)
	 */
	@Transactional
	public Project getProject(String projectName) {
		if (projectName == null) return null;
		/**
		 * we need a set to circumvent situations, where we get two nodes with
		 * identical name
		 * this shouldn't happen because of uniqueness, but it already has.
		 */
		Set<Project> temp = null;
			temp = projectRepository.findAllByName(projectName);
		if (temp.size() > 1) throw new IndexOutOfBoundsException("Only one Project should have been returned");
		if (temp.size() == 0) return null;
		Project result;
			result = projectRepository.findByName(projectName);
		return result;
	}

	/**
	 * search for a Component in Database, this could be any subclass of Component
	 * parameter component MUST have type attribute set to the desired type
	 * @param project  the result of getProject
	 * @param component a Component object where type and refID is set 
	 * @return the complete object with the data of the database OR NULL if nothing was found
	 */
	@Transactional
	public Component getComponent(Project project, Component component) {
		if (project == null || component == null) return null;

		Component temp = null;
		temp = ruleRepository.findByRefIDAndType(project.getName(),	component.getRefID(), component.getType());
		return temp;
	}
	
	/**
	 * search in database for a Constraint only by its name and the corresponding Project 
	 * @param project we need the exact project, because Components are only inside a project unique 
	 * @param refID which refID are we looking for
	 * @return 
	 */
	public Constraint getConstraint(Project project, String refID) {
		Component t = this.getComponent(project, new Constraint(refID));
		if (t == null) return null;
		return new Constraint(t);
	}

	/**
	 * search in database for a Concept only by its name and the corresponding Project 
	 * @param project we need the exact project, because Components are only inside a project unique 
	 * @param refID which refID are we looking for
	 * @return 
	 */
	public Concept getConcept(Project project, String refID) {
		Component t = this.getComponent(project, new Concept(refID));
		if (t == null) return null;
		return new Concept(t);
	}

	/**
	 * search in database for a Group only by its name and the corresponding Project 
	 * @param project we need the exact project, because Components are only inside a project unique 
	 * @param refID which refID are we looking for
	 * @return 
	 */
	public Group getGroup(Project project, String refID) {
		Component t = this.getComponent(project, new Group(refID));
		if (t == null) return null;
		return new Group(t);
	}

	/**
	 * search in database for a Template only by its name and the corresponding Project 
	 * @param project we need the exact project, because Components are only inside a project unique 
	 * @param refID which refID are we looking for
	 * @return 
	 */
	public QueryTemplate getTemplate(Project project, String refID) {
		Component t = this.getComponent(project, new QueryTemplate(refID));
		if (t == null) return null;
		return new QueryTemplate(t);
	}

	/**
	 * you have a component but have no clue about its source? This function will help you
	 * @param cmpt you need the component from the database, because were NEED the database-id (only refID wont work)
	 * @return
	 */
	public Project getComponentAssociatedProject(Component cmpt) {
		// returns project for a given component
		return ruleRepository.findProjectContaining(cmpt.getId());
	}

	/**
	 * this is for the global search auto-Completion, every component on this server
	 * @return
	 */
	@Transactional
	public Set<Component> getAllComponents() {
		Set<Component> result = null;
			result = ruleRepository.findAll();
		return result;
	}

	/**
	 * all Projects on the server, for the overview page mostly
	 * @return
	 */
	public Set<Project> getAllProjects() {
		Set<Project> allProjects = new HashSet<Project>();
		for (Project node : projectRepository.findAll()) {
			allProjects.add(node);
		}
		return allProjects;
	}

	/**
	 * Which users are allowed for a specific project
	 * @param project
	 * @return
	 */
	public Set<RegistredUser> getProjectUsers(Project project) {
		Set<RegistredUser> temp = new HashSet<RegistredUser>();
		if (project == null) throw new NullPointerException("Project ID cannot be null to find a User");

		temp = projectRepository.findUsers(project.getName());
		
		return temp;
	}

	/**
	 * search for all Components, which are referencing THIS component, this means looking in the upstream direction of the directed graph
	 * @param project
	 * @param component
	 * @return
	 */
	public Set<Component> getReferencingComponents(Project project,	Component component) {
		return ruleRepository.findUpstreamRefs(project.getName(), component.getRefID());
	}

	/**
	 * recursive search for ALL components references by a group 
	 * to generate a set of Components for XML output
	 * @param project
	 * @param g
	 * @return
	 */
	public Set<Component> getGroupComponents(Project project, Group g) {
		Set<Component> tempSet = ruleRepository.findAllReferencedNodes(project.getName(), g.getRefID());
		
		//get extRepos of Group and Project
		Set<String> repos = new HashSet<>();
		repos.addAll(g.getExternalRepos());
		repos.addAll(project.getExternalRepos());
		
		
		//iterate through all external Repos
		Iterator<String> repoIter = repos.iterator();
		while (repoIter.hasNext()) {
			String externalRepo = repoIter.next();

			_logic.validateExternalRepositoryAndThrowException(externalRepo);
			
			try {
				Set<Component> repoSet = _logic.XmlToObjectsFromUrl(externalRepo);
				tempSet.addAll(this.getIntersection(repoSet, tempSet, true));
			} catch (Exception e) {
				throw new IllegalArgumentException("Fehler beim Verarbeiten der externen Repos.");
			}
			
		}	
		
		return tempSet;
	}

	/**
	 * returns a Set of all Components of a single Project
	 * @param project
	 * @return 
	 */
	public Set<Component> getProjectComponents(Project project) {
		Set<Component> tempSet = ruleRepository.findAnyComponentOfProject(project.getName());
		
		//get extRepos of Group and Project
		Set<String> repos = new HashSet<>();
		repos.addAll(project.getExternalRepos());
		
		//iterate through all external Repos
		Iterator<String> repoIter = repos.iterator();
		while (repoIter.hasNext()) {
			String externalRepo = repoIter.next();
			
			_logic.validateExternalRepositoryAndThrowException(externalRepo);

			try {
				Set<Component> repoSet = _logic.XmlToObjectsFromUrl(externalRepo);
				tempSet.addAll(this.getIntersection(repoSet, tempSet, true));
			} catch (Exception e) {
				throw new IllegalArgumentException("Fehler beim Verarbeiten der externen Repos.");
			}			
		}	
		return tempSet;
	}
	
	/**
	 * returns a Set of all Components, referenced by c, which have NO other upstream
	 * references. That means, after deleting c, all components in this set will remain orphaned
	 * and so will have no further upstream connection -> unused
	 * @param p
	 * @param c
	 * @return
	 */
	public Set<Component> getSingleReferencedNodes(Project p, Component c) {
		Set<Component> result = new HashSet<>();
		
		//all contains every referenced by c... noSingles are these components with more than one upstream, so they wouldn't remain orphaned 
		Set<Component> all = c.getReferencedComponents();
		Set<Component> noSingles = ruleRepository.findSingleReferencedNodes(p.getName(), c.getRefID());
		
		Iterator<Component> iter1 = all.iterator();
		while (iter1.hasNext()) {
			Component temp = iter1.next();
			Iterator<Component> iter2 = noSingles.iterator();
			//we check every component in the "all" set and if there is no match in the noSingle List, we add it to the result, so the result is "all" without "noSingles"
			Boolean contained = false;
			while (iter2.hasNext()) {
				if (temp.getRefID().equals(iter2.next().getRefID())) contained = true;
			}
			if (!contained) result.add(temp);
		}
		return result;
	}
	
	/**
	 * Check two sets of Components against each other and return a set with duplicates
	 * Main goal for this method are external rules, imported as a set of components
	 * @param extern
	 * @param intern
	 * @param exclusive  - decide whether the returned set shall contain all duplicates (FALSE) or the opposite (TRUE, so everything not duplicated)
	 * @return
	 */
	public Set<Component> getIntersection(Set<Component> extern, Set<Component> intern, Boolean exclusive){
		if((intern==null)||(extern==null)) throw new NullPointerException("The Sets must not be null!");
	
		Set<Component> compset = new HashSet<Component>();
		if(exclusive) compset.addAll(extern);
		
		Iterator<Component> iterextern = extern.iterator();
		
		while(iterextern.hasNext()){
			Component externnext = iterextern.next();
			Iterator<Component> iterintern = intern.iterator();
			
			while(iterintern.hasNext()){
				Component internnext = iterintern.next();
				
				if(externnext.getRefID().equals(internnext.getRefID())){
					if (exclusive) {
						compset.remove(externnext);
					} else {
						compset.add(internnext);
					}
				}
			}
		}
	
		return compset;
	}
	

	/*************************************************************************
	 ************************* UPDATE/CREATE/DELETE****************************
	 *************************************************************************/

	/**
	 * given a project this method will either save an existing one or create a new one in the database if no entry is found
	 * @param project
	 * @return the project object from the database with contained database-id
	 */
	@Transactional
	public Project saveProject(Project project) {
		if (project == null) throw new NullPointerException("Project cannot be NULL to save it");
		Project temp = getProject(project.getName());
		if (temp == null) {
			// create a new one
				temp = projectRepository.save(project);
		} else {
			// update existing one
				temp.copyProject(project);
				temp = projectRepository.save(temp);
		}
		return temp;
	}

	/**
	 * save a component in the database or create a new one if not already existent
	 * @param project
	 * @param component
	 * @return the component with contained database-id for further actions
	 */
	@Transactional
	public Component saveComponent(Project project, Component component) {
		if (project == null || component == null) throw new NullPointerException("Component or Project cannot be NULL");
		Component c = getComponent(project, component);
		if (c == null) {
				c = ruleRepository.save(component);
				this.addComponentToProject(project, component);
		} else {
			// update existing entry
				c = ruleRepository.save(component);
		}
		return c;
	}

	
	/**
	 * CAUTION!
	 * This function will delete EVERYTHING connected to a project, so all components and parameters and references between them 
	 * @param project
	 */
	@Transactional
	public void deleteProject(Project project) {
		if (project == null) throw new NullPointerException("Cannot delete NULL project");
		if (project.getId() == null) throw new NullPointerException("Cannot delete project without ID");
		
		Set<Component> t = new HashSet<Component>(project.getComponents()); 
		Iterator<Component> t_iter = t.iterator();
		while (t_iter.hasNext()) {
			this.deleteComponent(project, t_iter.next());
		}
			projectRepository.delete(project.getId());
			if (projectRepository.findOne(project.getId()) != null) throw new RuntimeException("Entity Project " + project.getName() + " NOT deleted");
	}

	/**
	 * deletes a component and every associated parameter
	 * @param project
	 * @param component
	 */
	@Transactional
	public void deleteComponent(Project project, Component component) {
		if (component == null) throw new NullPointerException("Cannot delete NULL component");
		if (component.getId() == null) throw new NullPointerException("Cannot delete component without ID");

		// delete the reference from project
		if (project != null) project.deleteComponent(component);

		// we need to find all associated parameters and delete them in advance
		Set<Parameter> temp = component.getParameters();
			// start with parameters
			Iterator<Parameter> iter = temp.iterator();
			while (iter.hasNext()) {
				parameterRepository.delete(iter.next());
			}
			ruleRepository.delete(component.getId());
			if (ruleRepository.findOne(component.getId()) != null ) throw new RuntimeException("Entity Component " + component.getRefID() + " NOT deleted"); 
	}
	
	/**
	 * delete only a parameter from a component
	 * @param project
	 * @param component
	 * @param para
	 */
	@Transactional
	public void deleteParameter(Project project, Component component, Parameter para) {
		if (project == null) throw new NullPointerException("Project should not null!");
		if (component == null) throw new NullPointerException("Component should not null");
		if (para == null) throw new NullPointerException("Paramter should not be null!");
		
			component.deleteParameter(para);
			parameterRepository.delete(para.getId());
	}
	
	/**
	 * delete not one but all parameters of a given component, used for update parameters in the ComponentController
	 * @param project
	 * @param component
	 */
	@Transactional
	public void deleteAllParameters(Project project, Component component) {
		if (project == null) throw new NullPointerException("Project should not null!");
		if (component == null) throw new NullPointerException("Component should not null");
		
			Set<Parameter> tempSet = component.getParameters();
			Iterator<Parameter> iter = tempSet.iterator();
			while (iter.hasNext()) {
				parameterRepository.delete(iter.next().getId());
			component.setParameters(new HashSet<Parameter>());
		}
	}

	
/***************************************************************************
 ***************************REFERENCE**************************************
 ***************************************************************************/
	
	/**
	 * adds a reference in the GraphDatabase between two components 
	 * @param p - the project in which both components are located
	 * @param cmpt_source - source of the edge
	 * @param cmpt_dest - target (which means, the source component depends on this component)
	 */
	public void addComponentRef(Project p, Component cmpt_source, Component cmpt_dest) {
		//check if a cycle would be created or double referencing
		Component temp = ruleRepository.findAnyConnectionBetween(p.getName(), cmpt_source.getRefID(), cmpt_dest.getRefID());
		if (temp !=  null) throw new IllegalArgumentException("CYCLE! Cannot add " + cmpt_dest.getRefID() + " to " + cmpt_source.getRefID());
		//there is no existing connection between both nodes, so we can create a new one
		cmpt_source.addReference(cmpt_dest);
	}
	
	/**
	 * add a component or group to a group
	 * @param p - Project in which the group and component are located
	 * @param grp - the group
	 * @param cmpt - the component (which also could be a group)
	 * @param severity - overwrite the severity of the component only for that group (no change to the component)
	 */
	public void addGroupRef(Project p, Group grp, Component cmpt, String severity) {
		Component temp = ruleRepository.findAnyConnectionBetween(p.getName(), grp.getRefID(), cmpt.getRefID());
		if (temp !=  null) throw new IllegalArgumentException("CYCLE! Cannot add " + cmpt.getRefID() + " to " + grp.getRefID());
		grp.addReference(cmpt, "0"+severity);
	}
	
	/**
	 * delete a reference between two given nodes
	 * @param p
	 * @param cmpt_source
	 * @param cmpt_dest
	 */
	public void deleteReference(Project p, Component cmpt_source, Component cmpt_dest) {
		cmpt_source.deleteReference(cmpt_dest);
	}

	/**
	 * a component MUST have an associated project, this function is called every time a new component is saved.
	 * @param p
	 * @param cmpt
	 * @return
	 */
	@Transactional
	private boolean addComponentToProject(Project p, Component cmpt) {
		// check whether the component is already linked or not
		// Query for relation CONTAINS
		if (!p.addComponent(cmpt))
			return false;
			projectRepository.save(p);
		return true;
	}

}
