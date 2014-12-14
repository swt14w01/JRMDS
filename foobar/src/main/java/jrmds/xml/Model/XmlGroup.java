package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class XmlGroup
	extends XmlBaseIdElement {

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
	
	@XmlElement(name="concept1")
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
}
