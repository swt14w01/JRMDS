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
		if(this.name == null) return "";
		return name;
	}
	public String isString() {
		return isString;
	}
	public String getValue() {
		if(this.value == null) return "";
		return value;
	}
	
	public void setValue(String value, Boolean isString) {
		if(value == null) throw new NullPointerException("The value you want to set for this Parameter is null!");
		if(isString == null) throw new NullPointerException("You did not set a Type for the Parameter!");
		this.isString = "false";
		if (isString) this.isString = "true";
		this.value=value;
	}
	
}
