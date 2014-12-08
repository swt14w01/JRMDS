package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="jqassistant-rules",namespace="http://www.buschmais.com/jqassistant/core/analysis/rules/schema/v1.0")
public class XmlRule
	extends XmlGroup
{
	
	@JsonProperty("template")
	private Set<XmlTemplate> _templates;
    
	
	public Set<XmlTemplate> getTemplates()
	{
		return _templates;
	}
	
	@XmlElement(name="template")
	public void setTemplates(Set<XmlTemplate> templates)
	{
		_templates = templates;
	}


}
