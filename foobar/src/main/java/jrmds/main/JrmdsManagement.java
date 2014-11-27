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
		Set<Project> temp = null;
		try (Transaction tx = db.beginTx()) {
			temp = projectRepository.findAllByName(projectName);
			tx.success();
		}
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
		if (project==null || refID==null) return null;
		try (Transaction tx = db.beginTx()) {
			result = new Constraint(ruleRepository.findByRefID(refID,
					ComponentType.CONCEPT));
			tx.success();
		}
		return result;
	}

	public Concept getConcept(Project project, String refID) {
		if (project == null || refID == null) return null;
		Concept result = null;
		try (Transaction tx = db.beginTx()) {
			result = new Concept(ruleRepository.findByRefID(refID,
					ComponentType.CONCEPT));
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
			result = new Group(ruleRepository.findByRefID(refID,
					ComponentType.GROUP));
			tx.success();
		}
		return result;
	}

	public QueryTemplate getTemplate(Project project, String refID) {
		if (project == null || refID == null) return null;
		QueryTemplate result = null;
		try (Transaction tx = db.beginTx()) {
			result = new QueryTemplate(ruleRepository.findByRefID(refID,
					ComponentType.TEMPLATE));
			tx.success();
		}
		return result;
	}

	public Component getComponent(Project project, Component component) {
		if (project == null || component == null) return null;
		Component temp = null;
		try (Transaction tx = db.beginTx()) {
			temp = ruleRepository.findByRefID(component.getRefID(), component.getType());
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
		Component temp = getComponent(project, component);
		if (temp == null) {
			try (Transaction tx = db.beginTx()) {
				ruleRepository.save(component);
				tx.success();
			}
			return true;
		} else {
			// update bestehenden Eintrag
			try (Transaction tx = db.beginTx()) {
				temp.copy(component);
				ruleRepository.save(temp);
				tx.success();
			}
			return false;
		}
	}

	public boolean deleteProject(Project project) {
		boolean booli = false;
		try (Transaction tx = db.beginTx()) {
			projectRepository.delete(project.getId());
			if (!projectRepository.exists(project.getId()))
				booli = true;
			tx.success();
		}
		return booli;
	}

	public boolean deleteComponent(Project project, Component component) {
		boolean booli = false;
		try (Transaction tx = db.beginTx()) {
			ruleRepository.delete(component.getId());
			if (!ruleRepository.exists(component.getId()))
				booli = true;
			tx.success();
		}
		return booli;
		// what happens, if relations still persist from and to this component?
	}

	public Set<Component> referecedBy(Project project, Component component) {
		// find all Backlinks to this component (which COmponent is using THIS
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
		// returns a Set of all Components of a single Project
		Set<Component> temp = null;

		return temp;
	}

	public boolean addComponentToProject(Project p, Component cmpt) {
		//check wether the component is already linked or not
		//Query for releation CONTAINS
		if (!p.addComponent(cmpt)) return false;
		try (Transaction tx = db.beginTx()) {
			projectRepository.save(p);
			tx.success();
		}
		return true;
	}

}
