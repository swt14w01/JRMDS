package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * class to build a model of XMLRule, determine the order
 *
 */
@XmlRootElement(name="jqassistant-rules",namespace="http://www.buschmais.com/jqassistant/core/analysis/rules/schema/v1.0")
@XmlType(name="",propOrder={"groups", "concepts", "constraints", "templates"})
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
    
	
	/**
	 * get concepts
	 * @return
	 */
	public Set<XmlConcept> getConcepts()
	{
		return _concepts;
	}
	
	/**
	 * set concepts
	 * @param concepts
	 */
	@XmlElement(name="concept")
	public void setConcepts(Set<XmlConcept> concepts)
	{
		_concepts = concepts;
	}
	
	/**
	 * get constraints
	 * @return
	 */
	public Set<XmlConstraint> getConstraints()
	{
		return _constraints;
	}
	
	/**
	 * set constraints
	 * @param constraints
	 */
	@XmlElement(name="constraint")
	public void setConstraints(Set<XmlConstraint> constraints)
	{
		_constraints = constraints;
	}
	
	/**
	 * get groups
	 * @return
	 */
	public Set<XmlGroup> getGroups()
	{
		return _groups;
	}
	
	/**
	 * set groups
	 * @param groups
	 */
	@XmlElement(name="group")
	public void setGroups(Set<XmlGroup> groups)
	{
		_groups = groups;
	}
	
	/**
	 * get templates
	 * @return
	 */
	public Set<XmlTemplate> getTemplates()
	{
		return _templates;
	}
	
	/**
	 * set templates
	 * @param templates
	 */
	@XmlElement(name="template")
	public void setTemplates(Set<XmlTemplate> templates)
	{
		_templates = templates;
	}


}
