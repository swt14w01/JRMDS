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
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public Set<String> getExternalRepos() {
		return externalRepos;
	}

	public Set<Component> getComponents() {
		return componentSet;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addExternalRepo(String extRepo) {
		externalRepos.add(extRepo);
	}

	public void setExternalRepo(Set<String> extRepos) {
		externalRepos = extRepos;
	}

	public boolean deleteExternalRepo(String extRepo) {
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
		id = project.getId();
		name = project.getName();
		externalRepos = project.getExternalRepos();

	}

	public boolean equals(Project project) {
		if (project.getId().equals(id)) {
			return true;
		} else {
			return false;
		}
	}

	/*public int hashCode() {
		return id.intValue();
	}*/

	public String toString() {
		return name;
	}

}
