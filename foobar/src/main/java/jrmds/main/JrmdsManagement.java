package jrmds.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jrmds.model.Component;
import jrmds.model.ComponentType;
import jrmds.model.Concept;
import jrmds.model.Constraint;
import jrmds.model.Group;
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

	
	public Project getProject(String projectName) {
		if (projectName == null) return null;
		//we need a set to circumvent situations, where we get two nodes with identical name
		//this shouldnt happen because of uniqueness, but it already has.
		Set<Project> temp = null;
		try (Transaction tx = db.beginTx()) {
			temp = projectRepository.findAllByName(projectName);
			tx.success();
		}
		//return new Project as temporary solution, until a exception handler is implemented
		if (temp.size() > 1) return new Project("ERROR_MORE-THEN-ONE-PROJECT");
		if (temp.size() == 0) return null;
		Project result;
		try (Transaction tx = db.beginTx()) {
			result = projectRepository.findByName(projectName);
			tx.success();
		}
		return result;
	}

	public Constraint getConstraint(Project project, String refID) {
		if (project == null || refID == null) return null;
		Constraint result = null;
		try (Transaction tx = db.beginTx()) {
			result = new Constraint(ruleRepository.findByRefIDAndType(project.getName(), refID,ComponentType.CONCEPT));
			tx.success();
		}
		return result;
	}

	public Concept getConcept(Project project, String refID) {
		if (project == null || refID == null) return null;
		Concept result = null;
		try (Transaction tx = db.beginTx()) {
			result = new Concept(ruleRepository.findByRefIDAndType(project.getName(), refID, ComponentType.CONCEPT));
			tx.success();
		}
		return result;
	}

	
	public List<Component> getAllComponents(){
		List<Component> resultList= new ArrayList<>();
		try (Transaction tx = db.beginTx()) {
			for(Component component : ruleRepository.findAll()) {
				resultList.add(component);
			}
		}
		return resultList;
		
	}
	
	
	public Group getGroup(Project project, String refID) {
		if (project == null || refID == null) return null;
		Group result = null;
		try (Transaction tx = db.beginTx()) {
			result = new Group(ruleRepository.findByRefIDAndType(project.getName(), refID,ComponentType.GROUP));
			tx.success();
		}
		return result;
	}

	public QueryTemplate getTemplate(Project project, String refID) {
		if (project == null || refID == null) return null;
		QueryTemplate result = null;
		try (Transaction tx = db.beginTx()) {
			result = new QueryTemplate(ruleRepository.findByRefIDAndType(project.getName(), refID,ComponentType.TEMPLATE));
			tx.success();
		}
		return result;
	}

	public Component getComponent(Project project, Component component) {
		if (project == null || component == null) return null;
		Component temp = null;
		try (Transaction tx = db.beginTx()) {
			temp = ruleRepository.findByRefIDAndType(project.getName(), component.getRefID(), component.getType());
			tx.success();
		}
		return temp;
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

		return temp;
	}
	
	@Transactional
	public boolean saveProject(Project project) {
		if (project == null) return false;
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
				projectRepository.save(project);
				tx.success();
			}
		}
		return true;
	}

	public boolean saveComponent(Project project, Component component) {
		if (project.getId() == null || component == null) return false;
		Component temp = getComponent(project, component);
		if (temp == null) {
			try (Transaction tx = db.beginTx()) {
				ruleRepository.save(component);
				tx.success();
				return this.addComponentToProject(project, component);
			}
			
		} else {
			// update existing entry
			try (Transaction tx = db.beginTx()) {
				temp.copy(component);
				ruleRepository.save(temp);
				tx.success();
				return true;
			}
		}
	}

	public boolean deleteProject(Project project) {
		if (project.getId() == null) return false;
		if (project.getComponents().size() > 0) return false; 
		boolean booli = false;
		try (Transaction tx = db.beginTx()) {
			projectRepository.delete(project.getId());
			tx.success();
			if (this.getProject(project.getName()) == null)	booli = true;
		}
		return booli;
	}
	
	public Set<Component> getComponentUpstream(Project project, Component component) {
		//return every Component, that is referencing to this component
		return ruleRepository.findUpstreamRefs(project.getName(), component.getRefID());
	}
	
	public boolean deleteComponent(Project project, Component component) {
		boolean booli = false;
		//what happens, if relations still persist from AND to this component
		
		//get all upstream references, except project
		if (getComponentUpstream(project,component).size() > 0) return false;
		if (component.getReferencedComponents().size()>0) return false;
		try (Transaction tx = db.beginTx()) {
			ruleRepository.delete(component.getId());
			if (!ruleRepository.exists(component.getId()))
				booli = true;
			tx.success();
		}
		return booli;
		//what happens, if relations still persist from AND to this component?
	}

	public Set<Component> referecedBy(Project project, Component component) {
		// find all Backlinks to this component (which Component is using THIS
		// as dependency
		Set<Component> temp = new HashSet<Component>();

		return temp;
	}

	public Set<Component> getGroupComponents(Project project, Group g) {
		// returns a Set of EVERY Rule, to generate a Set of Components for XML
		// output
		Set<Component> temp = null;

		return temp;
	}

	public Set<Component> getProjectComponents(Project project) {
		//returns a Set of all Components of a single Project
		Set<Component> temp = null;

		return temp;
	}

	private boolean addComponentToProject(Project p, Component cmpt) {
		//check whether the component is already linked or not
		//Query for relation CONTAINS
		if (!p.addComponent(cmpt)) return false;
		try (Transaction tx = db.beginTx()) {
			projectRepository.save(p);
			tx.success();
		}
		return true;
	}

}
