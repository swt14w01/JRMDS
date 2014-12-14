package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="jqassistant-rules",namespace="http://www.buschmais.com/jqassistant/core/analysis/rules/schema/v1.0")
@XmlType(name="",propOrder={"groups", "concepts", "constraints"})
public class XmlRule
{
	
	@JsonProperty("template")
	private Set<XmlTemplate> _templates;
	
	@JsonProperty("concept")
    private Set<XmlConcept> _concepts;
    
	@JsonProperty("constraint")
	private Set<XmlConstraint> _constraints;
	
	@JsonProperty("group")
	private Set<XmlGroup> _groups;
    
	
	public Set<XmlConcept> getConcepts()
	{
		return _concepts;
	}
	
	@XmlElement(name="concept")
	public void setConcepts(Set<XmlConcept> concepts)
	{
		_concepts = concepts;
	}

	public Set<XmlConstraint> getConstraints()
	{
		return _constraints;
	}
	
	@XmlElement(name="constraint")
	public void setConstraints(Set<XmlConstraint> constraints)
	{
		_constraints = constraints;
	}
	
	public Set<XmlGroup> getGroups()
	{
		return _groups;
	}
	
	@XmlElement(name="group")
	public void setGroups(Set<XmlGroup> groups)
	{
		_groups = groups;
	}
	
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
