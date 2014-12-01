package jrmds.xml;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class XmlTemplate
{
	
	@JsonProperty("id")
	private String _id;
	@JsonProperty("parameter")
    private Set<XmlParameter> _params;
    
	
	Set<XmlParameter> getParameter()
	{
		return _params;
	}
	
	@XmlElement(name="parameterDefinition")
	void setParameter(Set<XmlParameter> params)
	{
		_params = params;
	}


}
