package jrmds.xml;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="jqassistant-rules",namespace="http://www.buschmais.com/jqassistant/core/analysis/rules/schema/v1.0")
public class XmlRule
	extends XmlGroup
{
	
	@JsonProperty("template")
    Set<XmlTemplate> _templates;
    
	
	Set<XmlTemplate> getTemplates()
	{
		return _templates;
	}
	
	@XmlElement(name="template")
	void setTemplates(Set<XmlTemplate> templates)
	{
		_templates = templates;
	}


}
