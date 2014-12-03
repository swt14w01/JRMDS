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
import jrmds.model.RegisteredUser;

import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class JrmdsManagement {
	@Autowired
	private SpringRestGraphDatabase db;
	@Autowired
	private RuleRepository ruleRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ParameterRepository parameterRepository;

	/*************************************************************************
	 ************************* GETTERS*****************************************
	 *************************************************************************/

	public Project getProject(String projectName) {
		if (projectName == null) return null;
		// we need a set to circumvent situations, where we get two nodes with
		// identical name
		// this shouldn't happen because of uniqueness, but it already has.
		Set<Project> temp = null;
		try (Transaction tx = db.beginTx()) {
			temp = projectRepository.findAllByName(projectName);
			tx.success();
		}
		if (temp.size() > 1) throw new IndexOutOfBoundsException("Only one Project should have been returned");
		if (temp.size() == 0) return null;
		Project result;
		try (Transaction tx = db.beginTx()) {
			result = projectRepository.findByName(projectName);
			tx.success();
		}
		return result;
	}

	public Component getComponent(Project project, Component component) {
		if (project == null || component == null) return null;

		Component temp = null;
		try (Transaction tx = db.beginTx()) {
			temp = ruleRepository.findByRefIDAndType(project.getName(),	component.getRefID(), component.getType());
			tx.success();
		}
		return temp;
	}

	public Constraint getConstraint(Project project, String refID) {
		return new Constraint(this.getComponent(project, new Constraint(refID)));
	}

	public Concept getConcept(Project project, String refID) {
		return new Concept(this.getComponent(project, new Concept(refID)));
	}

	public Group getGroup(Project project, String refID) {
		return new Group(this.getComponent(project, new Group(refID)));
	}

	public QueryTemplate getTemplate(Project project, String refID) {
		return new QueryTemplate(this.getComponent(project, new QueryTemplate(
				refID)));
	}

	public Project getComponentAssociatedProject(Component cmpt) {
		// returns project for a given component
		return ruleRepository.findProjectContaining(cmpt.getId());
	}

	public Set<Component> getAllComponents() {
		Set<Component> result = null;
		try (Transaction tx = db.beginTx()) {
			result = ruleRepository.findAll();
			tx.success();
		}
		return result;
	}

	public Set<Project> getAllProjects() {
		Set<Project> allProjects = new HashSet<Project>();
		for (Project node : projectRepository.findAll()) {
			allProjects.add(node);
		}
		return allProjects;
	}

	public Set<RegisteredUser> getProjectUsers(Project project) {
		Set<RegisteredUser> temp = new HashSet<RegisteredUser>();
		if (project == null) return temp;

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

	/*************************************************************************
	 ************************* UPDATE/CREATE/DELETE****************************
	 *************************************************************************/

	@Transactional
	public Project saveProject(Project project) {
		if (project == null) throw new NullPointerException();
		Project temp = getProject(project.getName());
		if (temp == null) {
			// create a new one
			try (Transaction tx = db.beginTx()) {
				temp = projectRepository.save(project);
				tx.success();
			}
		} else {
			// update existing one
			try (Transaction tx = db.beginTx()) {
				temp.copyProject(project);
				temp = projectRepository.save(temp);
				tx.success();
			}
		}
		return temp;
	}

	@Transactional
	public Component saveComponent(Project project, Component component) {
		if (project == null || component == null) throw new NullPointerException();
		Component c = getComponent(project, component);
		if (c == null) {
			try (Transaction tx = db.beginTx()) {
				c = ruleRepository.save(component);
				tx.success();
				this.addComponentToProject(project, component);
			}
		} else {
			// update existing entry
			try (Transaction tx = db.beginTx()) {
				c.copy(component);
				c = ruleRepository.save(c);
				tx.success();
			}
		}
		return c;
	}

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
		try (Transaction tx = db.beginTx()) {
			projectRepository.delete(project.getId());
			if (projectRepository.findOne(project.getId()) != null) throw new RuntimeException("Entity Project " + project.getName() + " NOT deleted");
			tx.success();
		
		}
	}

	public void deleteComponent(Project project, Component component) {
		if (component == null) throw new NullPointerException("Cannot delete NULL component");
		if (component.getId() == null) throw new NullPointerException("Cannot delete component without ID");

		// delete the reference from project
		if (project != null) project.deleteComponent(component);

		// we need to find all associated parameters and delete them in advance
		Set<Parameter> temp = component.getParameters();
		try (Transaction tx = db.beginTx()) {
			// start with parameters
			Iterator<Parameter> iter = temp.iterator();
			while (iter.hasNext()) {
				parameterRepository.delete(iter.next());
			}
			ruleRepository.delete(component.getId());
			if (ruleRepository.findOne(component.getId()) != null ) throw new RuntimeException("Entity Component " + component.getRefID() + " NOT deleted"); 
			tx.success();
		}

		// what happens, if relations still persist from AND to this component?
	}

	
/***************************************************************************
 ***************************REFERENCE**************************************
 ***************************************************************************/
	public void addComponentRef(Project p, Component cmpt_source, Component cmpt_dest) {
		//check if a cycle would be created or double referencing
		Component temp = ruleRepository.findAnyConnectionBetween(p.getName(), cmpt_source.getRefID(), cmpt_dest.getRefID());
		if (temp !=  null) throw new IllegalArgumentException("Cycle! Cannot add " + cmpt_dest.getRefID() + " to " + cmpt_source.getRefID());

		//there is no existing connection between both nodes, so we can create a new one
		cmpt_source.addReference(cmpt_dest);
	}
	
	public void addGroupRef(Project p, Group grp, Component cmpt, String severity) {
		Component temp = ruleRepository.findAnyConnectionBetween(p.getName(), grp.getRefID(), cmpt.getRefID());
		if (temp !=  null) throw new IllegalArgumentException("Cycle! Cannot add " + cmpt.getRefID() + " to " + grp.getRefID());

		grp.addReference(cmpt, severity);

	}

	private boolean addComponentToProject(Project p, Component cmpt) {
		// check whether the component is already linked or not
		// Query for relation CONTAINS
		if (!p.addComponent(cmpt))
			return false;
		try (Transaction tx = db.beginTx()) {
			projectRepository.save(p);
			tx.success();
		}
		return true;
	}

}
