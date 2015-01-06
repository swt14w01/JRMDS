package jrmds.model;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Project {
	@GraphId
	private Long id;
	@Indexed(unique = true)
	private String name;
	private String description;
	private Set<String> externalRepos;
	@RelatedTo(type = "CONTAINS", direction = Direction.BOTH)
	private @Fetch Set<Component> componentSet;

	public Project() {
	}

	public Project(String name) {
		this.name = name;
		externalRepos = new HashSet<String>();
		componentSet = new HashSet<Component>();
	}

	public Project(String name, String description) {
		this.name = name;
		this.description = description;
		externalRepos = new HashSet<String>();
		componentSet = new HashSet<Component>();
	}

	public Long getId() {
		if(this.id == null) throw new NullPointerException("This Project's ID is null!");
		return id;
	}

	public String getName() {
		if(this.name == null) throw new NullPointerException("This Project's name is null!");
		return name;
	}

	public String getDescription() {
		if(this.description == null) return "";
		return description;
	}
	
	public Set<String> getExternalRepos() {
		if(this.externalRepos == null) return new HashSet<String>();
		return externalRepos;
	}

	public Set<Component> getComponents() {
		if(this.componentSet == null) return new HashSet<Component>();
		return componentSet;
	}

	public void setId(Long id) {
		if (id == null) throw new NullPointerException("The id you want to set for this project must not be null!");
		this.id = id;
	}

	public void setName(String name) {
		if (name == null) throw new NullPointerException("The name you want to set for this Project must not be null!");
		this.name = name;
	}

	public void setDescription(String description) {
		if (description == null) {
			this.description ="";
			return;
		}
		
		this.description = description;
	}
	
	public void addExternalRepo(String extRepo) {
		if(externalRepos == null) externalRepos = new HashSet<String>();
		externalRepos.add(extRepo);
	}

	public void setExternalRepo(Set<String> extRepos) {
		if (extRepos == null) throw new NullPointerException("The external Repository you want to add must not be null!");
		if(externalRepos == null) externalRepos = new HashSet<String>();
		externalRepos = extRepos;
	}

	public boolean deleteExternalRepo(String extRepo) {
		if (extRepo == null) throw new NullPointerException("You are trying to delete a Repository, which is null!!");
		if(externalRepos == null) externalRepos = new HashSet<String>();
		return externalRepos.remove(extRepo);
	}

	public boolean addComponent(Component cmpt) {
		if (cmpt == null) return false;
		if (componentSet==null) componentSet=new HashSet<Component>();
		return componentSet.add(cmpt);
	}

	public boolean deleteComponent(Component cmpt) {
		if (cmpt == null)
			return false;
		if (!componentSet.contains(cmpt))
			return true;
		componentSet.remove(cmpt);
		return true;
	}

	public void copyProject(Project project) {
		if(project==null) throw new NullPointerException("You are trying to copy a project, which is null!");
		name = project.getName();
		description = project.getDescription();
		externalRepos = project.getExternalRepos();
		if (componentSet == null) {
			componentSet = project.getComponents();
		} else {
			componentSet.addAll(project.getComponents());
		}
		
	}

	public boolean equals(Project project) {
		if (project.getId().equals(id)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	public int hashCode() {
		return id.intValue();
	}*/

	public String toString() {
		return name;
	}

}
