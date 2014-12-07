package jrmds.model;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Parameter {
	@GraphId private Long id;
	private String isString;
	private String name;
	private String value;
	
	public Parameter() {
		//empty for Hibernate
	}
	
	public Parameter(String name, String value, Boolean isString) {
		this.isString = "false";
		if (isString) this.isString = "true";
		this.name=name;
		this.value=value;
	}
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	public String isString() {
		return isString;
	}
	public String getValue() {
		return value;
	}
	
	public void setValue(String value, Boolean isString) {
		this.isString = "false";
		if (isString) this.isString = "true";
		this.value=value;
	}
	
}
