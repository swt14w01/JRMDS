package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * class for building the XmlModel of Parameter
 * 
 *
 */
public class XmlParameter
{

	@JsonProperty("name")
	private String _name;
	@JsonProperty("value")
	private String _value;
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
	 * get the value
	 * @return
	 */
	// TODO: Wie ausgeben?
	public String getValue() {
		return _value;
	}

	/**
	 * set the value
	 * @param value
	 */
	@XmlAttribute(name="value")
	public void setValue(String value) {
		_value = value;
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
