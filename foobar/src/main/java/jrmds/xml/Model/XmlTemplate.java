package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class XmlTemplate
{
	
	@JsonProperty("id")
	private String _id;
	@JsonProperty("parameter")
    private Set<XmlParameter> _params;
    
	
	public Set<XmlParameter> getParameter()
	{
		return _params;
	}
	
	@XmlElement(name="parameterDefinition")
	public void setParameter(Set<XmlParameter> params)
	{
		_params = params;
	}


}
