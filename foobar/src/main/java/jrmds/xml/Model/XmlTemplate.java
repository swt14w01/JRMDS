package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * class to build object of XML Template
 *
 */
public class XmlTemplate
{
	
	@JsonProperty("id")
	private String _id;
	@JsonProperty("parameter")
    private Set<XmlParameter> _params;
    
	/**
	 * get parameter
	 * @return
	 */
	public Set<XmlParameter> getParameter()
	{
		return _params;
	}
	
	/**
	 * set parameter
	 * @param params
	 */
	@XmlElement(name="parameterDefinition")
	public void setParameter(Set<XmlParameter> params)
	{
		_params = params;
	}


}
