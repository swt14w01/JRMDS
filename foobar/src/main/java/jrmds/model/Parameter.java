package jrmds.model;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Parameter objects are in Concepts, Constraints and Templates. 
 */
@NodeEntity
public class Parameter {
	/** The Id of the Parameter for the GraphDatabase.*/
	@GraphId private Long id;
	/** A String which sets the Parameter as a String or an Int.*/
	private String isString;
	/** The name of the Parameter object.*/
	private String name;
	/** The value the Parameter has.*/
	private String value;
	
	/** Empty for Hibernate */
	public Parameter() {
	}
	
	/**
	 * The constructor creates a new Parameter object with the given parameters.
	 * @param name  
	 * @param value
	 * @param isString
	 */
	public Parameter(String name, String value, Boolean isString) {
		this.name=name;
		this.value=value;
		setIsString(isString);
	}
	
	/**
	 * Gets the current id of the Parameter object.
	 * @return id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Gets the current name of the Parameter object.
	 * @return name
	 */
	public String getName() {
		if(this.name == null) return "";
		return name;
	}
	
	/**
	 * Gets the current isString value of the Parameter object.
	 * @return isString
	 */
	public String isString() {
		return isString;
	}
	
	/**
	 * Gets the current value of the Parameter object.
	 * @return value
	 */
	public String getValue() {
		if(this.value == null) return "";
		return value;
	}

	public void setIsString(Boolean value)
	{
		if(value == null)
			throw new NullPointerException("You did not set a Type for the Parameter!");
		this.isString = value.toString().toLowerCase();
	}
	
	/**
	 * Sets the current value and isString with the given parameters value and isString.
	 * @param value  The given value which is used to overwrite the current value.
	 * @param isString  The given isString which is used to overwrite the current isString.
	 * @throws NullPointerException if value or isString is null.
	 */
	public void setValue(String value, Boolean isString) {
		if(value == null)
			throw new NullPointerException("The value you want to set for this Parameter is null!");
		this.value = value;

		setIsString(isString);
	}
	
}
