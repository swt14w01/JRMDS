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

	/*************************************************************************
	 ************************* GETTERS*****************************************
	 *************************************************************************/
	
	@Transactional
	public Project getProject(String projectName) {
		if (projectName == null) return null;
		// we need a set to circumvent situations, where we get two nodes with
		// identical name
		// this shouldn't happen because of uniqueness, but it already has.
		Set<Project> temp = null;
			temp = projectRepository.findAllByName(projectName);

		if (temp.size() > 1) throw new IndexOutOfBoundsException("Only one Project should have been returned");
		if (temp.size() == 0) return null;
		Project result;
			result = projectRepository.findByName(projectName);
		return result;
	}

	@Transactional
	public Component getComponent(Project project, Component component) {
		if (project == null || component == null) return null;

		Component temp = null;
			temp = ruleRepository.findByRefIDAndType(project.getName(),	component.getRefID(), component.getType());
		return temp;
	}

	public Constraint getConstraint(Project project, String refID) {
		Component t = this.getComponent(project, new Constraint(refID));
		if (t == null) return null;
		return new Constraint(t);
	}

	public Concept getConcept(Project project, String refID) {
		Component t = this.getComponent(project, new Concept(refID));
		if (t == null) return null;
		return new Concept(t);
	}

	public Group getGroup(Project project, String refID) {
		Component t = this.getComponent(project, new Group(refID));
		if (t == null) return null;
		return new Group(t);
	}

	public QueryTemplate getTemplate(Project project, String refID) {
		Component t = this.getComponent(project, new QueryTemplate(refID));
		if (t == null) return null;
		return new QueryTemplate(t);
	}

	public Project getComponentAssociatedProject(Component cmpt) {
		// returns project for a given component
		return ruleRepository.findProjectContaining(cmpt.getId());
	}

	@Transactional
	public Set<Component> getAllComponents() {
		Set<Component> result = null;
			result = ruleRepository.findAll();
		return result;
	}

	public Set<Project> getAllProjects() {
		Set<Project> allProjects = new HashSet<Project>();
		for (Project node : projectRepository.findAll()) {
			allProjects.add(node);
		}
		return allProjects;
	}

	public Set<RegistredUser> getProjectUsers(Project project) {
		Set<RegistredUser> temp = new HashSet<RegistredUser>();
		if (project == null) throw new NullPointerException("Project ID cannot be null to find a User");

		//MISSING UNTIL USERS ARE IMPLEMENTED
		
		return temp;
	}

	public Set<Component> getReferencingComponents(Project project,	Component component) {
		/**
		 * search for all Components, which are referencing THIS component
		 */
		return ruleRepository.findUpstreamRefs(project.getName(), component.getRefID());
	}

	public Set<Component> getGroupComponents(Project project, Group g) {
		// returns a Set of EVERY Rule, to generate a Set of Components for XML
		// output
		return ruleRepository.findAllReferencedNodes(project.getName(), g.getRefID());
	}

	public Set<Component> getProjectComponents(Project project) {
		//returns a Set of all Components of a single Project
		Set<Component> temp = ruleRepository.findAnyComponentOfProject(project.getName());
		return temp;
	}
	
	public Set<Component> getSingleReferencedNodes(Project p, Component c) {
		/**
		 * returns a Set of all Componenents, referenced by c, which have NO other upstream
		 * references. That means, after deleting c, all components in this set will remain orphaned
		 */
		Set<Component> result = new HashSet<>();
		
		//all contains every referenced... noSingles are theese components with more than one upstream
		Set<Component> all = c.getReferencedComponents();
		Set<Component> noSingles = ruleRepository.findSingleReferencedNodes(p.getName(), c.getRefID());
		
		Iterator<Component> iter1 = all.iterator();
		while (iter1.hasNext()) {
			Component temp = iter1.next();
			Iterator<Component> iter2 = noSingles.iterator();
			//we check every component in the "all" set and if there is no match in th noSingle List, we add it to the result
			Boolean contained = true;
			while (iter2.hasNext()) {
				if (temp.getRefID().equals(iter2.next().getRefID())) contained = false;
			}
			if (contained) result.add(temp);
		}
		return result;
	}
	
	public Set<Component> getIntersection(Set<Component> extern, Set<Component> intern, Boolean exclusive){
		
		
		if((intern==null)||(extern==null)) throw new NullPointerException("The Sets must not be null!");
		
		//Set, in das Mengen eingespeichert werden
		Set<Component> compset = new HashSet<Component>();
		if(exclusive == true) compset.addAll(extern);
		
		Iterator<Component> iterextern = extern.iterator();
		
		//durch die Sets wandern
			while(iterextern.hasNext()){
				Component externnext = iterextern.next();
				Iterator<Component> iterintern = intern.iterator();
				
				while(iterintern.hasNext()){
					Component internnext = iterintern.next();
					
					if(externnext.getRefID() == internnext.getRefID()){
						//Extern ohne intern
						if(exclusive == true){
							 compset.remove(internnext);
						}
						//Schnittmenge extern und intern
						if(exclusive ==false) {
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

	@Transactional
	public void deleteProject(Project project) {
		/**
		 * be VERY careful with this function. EVERY contained Component will be deleted!
		 */
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
	
	@Transactional
	public void deleteParameter(Project project, Component component, Parameter para) {
		/**
		 * delete a parameter object from a Component
		 */
		if (project == null) throw new NullPointerException("Project should not null!");
		if (component == null) throw new NullPointerException("Component should not null");
		if (para == null) throw new NullPointerException("Paramter should not be null!");
		
			component.deleteParameter(para);
			parameterRepository.delete(para.getId());
	}
	
	@Transactional
	public void deleteAllParameters(Project project, Component component) {
		/**
		 * delete every parameter from an component, used for parameter updating
		 */
		if (project == null) throw new NullPointerException("Project should not null!");
		if (component == null) throw new NullPointerException("Component should not null");
		
			Set<Parameter> tempSet = component.getParameters();
			Iterator<Parameter> iter = tempSet.iterator();
			while (iter.hasNext()) {
				parameterRepository.delete(iter.next().getId());
			}
			component.setParameters(new HashSet<Parameter>());
	}

	
/***************************************************************************
 ***************************REFERENCE**************************************
 ***************************************************************************/
	public void addComponentRef(Project p, Component cmpt_source, Component cmpt_dest) {
		//check if a cycle would be created or double referencing
		Component temp = ruleRepository.findAnyConnectionBetween(p.getName(), cmpt_source.getRefID(), cmpt_dest.getRefID());
		if (temp !=  null) throw new IllegalArgumentException("CYCLE! Cannot add " + cmpt_dest.getRefID() + " to " + cmpt_source.getRefID());
		//there is no existing connection between both nodes, so we can create a new one
		cmpt_source.addReference(cmpt_dest);
	}
	
	public void addGroupRef(Project p, Group grp, Component cmpt, String severity) {
		Component temp = ruleRepository.findAnyConnectionBetween(p.getName(), grp.getRefID(), cmpt.getRefID());
		if (temp !=  null) throw new IllegalArgumentException("CYCLE! Cannot add " + cmpt.getRefID() + " to " + grp.getRefID());
		grp.addReference(cmpt, severity);
	}
	
	public void deleteReference(Project p, Component cmpt_source, Component cmpt_dest) {
		cmpt_source.deleteReference(cmpt_dest);
	}

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
