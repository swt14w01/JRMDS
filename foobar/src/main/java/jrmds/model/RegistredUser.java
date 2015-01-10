package jrmds.model;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.Fetch;

@NodeEntity
public class RegistredUser {
	@GraphId private Long id;
	@Indexed(unique = true)
	private String username;
	private String password;
	private String emailAdress;
	@RelatedTo(type="WORKSON", direction=Direction.BOTH)
	private @Fetch Set<Project> projects;
	
	public RegistredUser() {}
	
	public RegistredUser(RegistredUser registredUser) {
		this.id = registredUser.getID();
		this.username = registredUser.getUsername();
		this.password = registredUser.getPassword();
		this.emailAdress = registredUser.getEmailAdress();
	}
	
	public RegistredUser(String username, String password, String emailAdress) {
		this.username = username;
		this.password = password;
		this.emailAdress = emailAdress;
	}
	

	public Boolean addProject(Project project) {
        if (projects == null) {
            projects = new HashSet<Project>(); 
        }
        if (projects.contains(project)) {
        	return false;
        } else {
        	projects.add(project);
        	 return true;
        }
        }
            
	public Set<Project> getProjects() {
		return projects;
	}
	
	public Boolean worksOn(Project project) {
		Boolean t = false;
		for (Project p : projects) {
			if (p.getName().equals(project.getName())) t = true;
		}
		return t;
	}
	
	public Long getID() {
		return id;
	}
	public String getUsername() {
		return this.username;
	}
	public String getPassword() {
		return this.password;
	}
	public String getEmailAdress() {
		return this.emailAdress;
	}
	
	public String getName() {
		return this.username;
	}
	
	public void setUsername(String newUsername) {
		this.username = newUsername;
	}
	public void setPassword(String newPassword) {
		this.password = newPassword;
	}
	public void setEmailAdress(String newEmailAdress) {
		this.emailAdress = newEmailAdress;
	}
}

