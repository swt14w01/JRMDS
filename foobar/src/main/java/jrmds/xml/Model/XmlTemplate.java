package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * class to build object of XML Template
 *
 */
@XmlType(name="",propOrder={"requiresConcept", "description", "cypher", "parameterDefinition"})
public class XmlTemplate
	extends XmlBaseElement
{
	
	@JsonProperty("parameterDefinition")
    private Set<XmlParameterDefinition> _params;
    
	/**
	 * get parameter
	 * @return
	 */
	public Set<XmlParameterDefinition> getParameterDefinition()
	{
		return _params;
	}
	
	/**
	 * set parameter
	 * @param params
	 */
	@XmlElement(name="parameterDefinition")
	public void setParameterDefinition(Set<XmlParameterDefinition> params)
	{
		_params = params;
	}


}
