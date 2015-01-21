package jrmds.model;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * This class represents all Project objects.
 */
@NodeEntity
public class Project {
	/** The Id of the Project for the GraphDatabase.*/
	@GraphId
	private Long id;
	/** A unique name for the project for the intern methods.*/
	@Indexed(unique = true)
	private String name;
	/** A description of the project. */
	private String description;
	/** A Set of all external Repositories of the Project */
	private Set<String> externalRepos;
	/** A Set of all Components of the Project */
	@RelatedTo(type = "CONTAINS", direction = Direction.BOTH)
	private @Fetch Set<Component> componentSet;

	/** Empty for Hibernate */
	public Project() {
	}

	/** 
	 * Constructor to create a new Project.
	 * @param name	The name for the new Project.
	 */
	public Project(String name) {
		this.name = name;
		externalRepos = new HashSet<String>();
		componentSet = new HashSet<Component>();
	}

	/**
	 *  * Constructor to create a new Project.
	 * @param name	The name for the new Project.
	 * @param description	The description for the Project.
	 */
	public Project(String name, String description) {
		this.name = name;
		this.description = description;
		externalRepos = new HashSet<String>();
		componentSet = new HashSet<Component>();
	}

	/**
	 * Gets the Id of the Project,
	 * @return id
	 * @throws NullPointerException if the id is null.
	 */
	public Long getId() {
		if(this.id == null) throw new NullPointerException("This Project's ID is null!");
		return id;
	}

	/**
	 * Gets the name of the Project.
	 * @return name
	 * @throws NullPointerException if the name is null.
	 */
	public String getName() {
		if(this.name == null) throw new NullPointerException("This Project's name is null!");
		return name;
	}
	/**
	 * Gets the description of the project.
	 * @return description or "" if it is null
	 */
	public String getDescription() {
		if(this.description == null) return "";
		return description;
	}
	
	/**
	 * Gets the external Repositories of the project.
	 * @return externalRepos or an empty HashSet<String> if null.
	 */
	public Set<String> getExternalRepos() {
		if(this.externalRepos == null) return new HashSet<String>();
		return externalRepos;
	}

	/**
	 * Gets all Components of the project.
	 * @return componentSet or an emtpy HashSet<Component> if null.
	 */
	public Set<Component> getComponents() {
		if(this.componentSet == null) return new HashSet<Component>();
		return componentSet;
	}

	/**
	 * Sets the current id with the given id.
	 * @param id  given id.
	 * @throws NullPointerException if id is null.
	 */
	public void setId(Long id) {
		if (id == null) throw new NullPointerException("The id you want to set for this project must not be null!");
		this.id = id;
	}

	/**
	 * Sets the current name with the given name.
	 * @param name  given name.
	 * @throws NullPointerException if name is null.
	 */
	public void setName(String name) {
		if (name == null) throw new NullPointerException("The name you want to set for this Project must not be null!");
		this.name = name;
	}

	/**
	 * Sets the current description with the given description or "" if it is null.
	 * @param description  given name.
	 */
	public void setDescription(String description) {
		if (description == null) {
			this.description ="";
			return;
		}
		this.description = description;
	}
	
	/**
	 * Adds a given external Repository to the current externalRepos.
	 * @param extRepo  given external Repository
	 */
	public void addExternalRepo(String extRepo) {
		if(externalRepos == null) externalRepos = new HashSet<String>();
		externalRepos.add(extRepo);
	}

	/**
	 * Adds a Set of given external Repositories to the current externalRepos.
	 * @param extRepos given external Repositories
	 * @throws NullPointerException if extRepos is null.
	 */
	public void setExternalRepo(Set<String> extRepos) {
		if (extRepos == null) throw new NullPointerException("The external Repository you want to add must not be null!");
		if(externalRepos == null) externalRepos = new HashSet<String>();
		externalRepos = extRepos;
	}

	/**
	 * Deletes a given external Repository in the current exteralRepos
	 * @param extRepo  given external Repository
	 * @return true if it was removed, false if not
	 * @throws NullPointerException if extRepo is null
	 */
	public boolean deleteExternalRepo(String extRepo) {
		if (extRepo == null) throw new NullPointerException("You are trying to delete a Repository, which is null!!");
		if(externalRepos == null) externalRepos = new HashSet<String>();
		return externalRepos.remove(extRepo);
	}

	/**
	 * Adds a given Component to the current componentSet.
	 * @param cmpt given Component
	 * @return true, if added, false if not
	 */
	public boolean addComponent(Component cmpt) {
		if (cmpt == null) return false;
		if (componentSet==null) componentSet=new HashSet<Component>();
		return componentSet.add(cmpt);
	}

	/**
	 * Deletes the given Component out of the current componentSet.
	 * @param cmpt  given Component
	 * @return true if removed, false if cmpt was null.
	 */
	public boolean deleteComponent(Component cmpt) {
		if (cmpt == null)
			return false;
		if (!componentSet.contains(cmpt))
			return true;
		componentSet.remove(cmpt);
		return true;
	}

	/**
	 * Copies all values of a  given project into the current one.
	 * @param project  given project
	 * @throws NullPointerException if the given project is null.
	 */
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

	/**
	 * Checks if the if of the given project equals the id of the current project.
	 * @param project given project
	 * @return true if equal, false if not.
	 */
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

	/**
	 * If project.toString() is called, this returns the name of the project.
	 */
	public String toString() {
		return name;
	}

}
