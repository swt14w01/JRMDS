package jrmds.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
		if (projectName == null)
			return null;
		// we need a set to circumvent situations, where we get two nodes with
		// identical name
		// this shouldn't happen because of uniqueness, but it already has.
		Set<Project> temp = null;
		try (Transaction tx = db.beginTx()) {
			temp = projectRepository.findAllByName(projectName);
			tx.success();
		}
		// return new Project as temporary solution, until a exception handler
		// is implemented
		if (temp.size() > 1)
			return new Project("ERROR_MORE-THEN-ONE-PROJECT");
		if (temp.size() == 0)
			return null;
		Project result;
		try (Transaction tx = db.beginTx()) {
			result = projectRepository.findByName(projectName);
			tx.success();
		}
		return result;
	}

	public Component getComponent(Project project, Component component) {
		if (project == null || component == null)
			return null;

		Component temp = null;
		try (Transaction tx = db.beginTx()) {
			temp = ruleRepository.findByRefIDAndType(project.getName(),
					component.getRefID(), component.getType());
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

	public List<Component> getAllComponents() {
		List<Component> resultList = new ArrayList<>();
		try (Transaction tx = db.beginTx()) {
			for (Component component : ruleRepository.findAll()) {
				resultList.add(component);
			}
		}
		return resultList;
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
		if (project == null)
			return temp;

		return temp;
	}

	public Set<Component> getReferencingComponents(Project project,
			Component component) {
		// return every Component, that is referencing to this component
		return ruleRepository.findUpstreamRefs(project.getName(),
				component.getRefID());
	}

	public Set<Component> getGroupComponents(Project project, Group g) {
		// returns a Set of EVERY Rule, to generate a Set of Components for XML
		// output
		Set<Component> temp = ruleRepository.findAllReferencedNodes(project.getName(), g.getRefID());

		return temp;
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
	public boolean saveProject(Project project) {
		if (project == null)
			return false;
		Project temp = getProject(project.getName());
		if (temp == null) {
			// create a new one
			try (Transaction tx = db.beginTx()) {
				projectRepository.save(project);
				tx.success();
			}
		} else {
			// update existing one
			try (Transaction tx = db.beginTx()) {
				temp.copyProject(project);
				projectRepository.save(temp);
				tx.success();
			}
		}
		return true;
	}

	@Transactional
	public boolean saveComponent(Project project, Component component) {
		if (project == null || component == null)
			throw new NullPointerException();
		Component c = getComponent(project, component);
		if (c == null) {
			try (Transaction tx = db.beginTx()) {
				c = ruleRepository.save(component);
				tx.success();
				return this.addComponentToProject(project, component);
			}
			
		} else {
			// update existing entry
			try (Transaction tx = db.beginTx()) {
				c.copy(component);
				ruleRepository.save(c);
				tx.success();
				return true;
			}
		}
	}

	public boolean deleteProject(Project project) {
		if (project == null)
			return false;
		if (project.getId() == null)
			return false;
		// if (project.getComponents().size() > 0) return false;
		// if you fire this up, every component of the project will be deleted!
		boolean booli = false;
		Set<Component> t = new HashSet<Component>(project.getComponents()); // we
																			// need
																			// a
																			// copy
																			// of
																			// the
																			// set,
																			// because
																			// deleteComponent
																			// removes
																			// entries
																			// from
																			// this
																			// list
		Iterator<Component> t_iter = t.iterator();
		while (t_iter.hasNext()) {
			this.deleteComponent(project, t_iter.next());
		}
		try (Transaction tx = db.beginTx()) {
			projectRepository.delete(project.getId());
			tx.success();
			if (this.getProject(project.getName()) == null)
				booli = true;
		}
		return booli;
	}

	public boolean deleteComponent(Project project, Component component) {
		if (component == null)
			return false;
		if (component.getId() == null)
			return false;
		boolean booli = false;

		// delete the reference from project
		if (project != null)
			project.deleteComponent(component);

		// we need to find all associated parameters and delete them in advance
		Set<Parameter> temp = component.getParameters();
		try (Transaction tx = db.beginTx()) {
			// start with parameters
			Iterator<Parameter> iter = temp.iterator();
			while (iter.hasNext()) {
				parameterRepository.delete(iter.next());
			}
			ruleRepository.delete(component.getId());
			if (!ruleRepository.exists(component.getId()))
				booli = true;
			tx.success();
		}
		return booli;
		// what happens, if relations still persist from AND to this component?
	}

	
/***************************************************************************
 ***************************REFERENCE**************************************
 ***************************************************************************/
	public boolean addComponentRef(Project p, Component cmpt_source, Component cmpt_dest) {
		//check if a cycle would be created or double referencing
		Component temp = ruleRepository.findAnyConnectionBetween(p.getName(), cmpt_source.getRefID(), cmpt_dest.getRefID());
		if (temp ==  null) {
			//there is no existing connection between both nodes, so we can create a new one
			return cmpt_source.addReference(cmpt_dest);
		}
		return false;
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
