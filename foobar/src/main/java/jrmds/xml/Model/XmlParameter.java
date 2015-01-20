package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * class for building the XmlModel of Parameter
 * 
 *
 */
public class XmlParameter
	extends XmlParameterDefinition
{

	@JsonProperty("value")
	private String _value;
	

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

}
