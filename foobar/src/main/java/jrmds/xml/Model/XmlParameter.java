package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;

import com.fasterxml.jackson.annotation.JsonProperty;


public class XmlParameter
{

	@JsonProperty("name")
	private String _name;
	@JsonProperty("value")
	private String _value;
	@JsonProperty("type")
	private String _type;
	

	@XmlAttribute(name="name")
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	// TODO: Wie ausgeben?
	public String getValue() {
		return _value;
	}

	@XmlAttribute(name="value")
	public void setValue(String value) {
		_value = value;
	}

	@XmlAttribute(name="type")
	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}
}
