package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;

import com.fasterxml.jackson.annotation.JsonProperty;

public class XmlParameterDefinition {

	@JsonProperty("name")
	private String _name;
	@JsonProperty("type")
	private String _type;
	
	/**
	 * get name of parameter element
	 * @return
	 */
	@XmlAttribute(name="name")
	public String getName() {
		return _name;
	}

	/**
	 * set the name
	 * @param name
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * get the type
	 * @return
	 */
	@XmlAttribute(name="type")
	public String getType() {
		return _type;
	}

	/**
	 * set the type
	 * @param type
	 */
	public void setType(String type) {
		_type = type;
	}
}
