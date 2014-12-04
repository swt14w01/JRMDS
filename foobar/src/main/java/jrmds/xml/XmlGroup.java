package jrmds.xml;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class XmlGroup {
	@JsonProperty("concept")
    Set<XmlConcept> _concepts;
    
	@JsonProperty("constraint")
    Set<XmlConstraint> _constraints;
	
	@JsonProperty("group")
    Set<XmlGroup> _groups;
    
	
	Set<XmlConcept> getConcepts()
	{
		return _concepts;
	}
	
	@XmlElement(name="concept")
	void setConcepts(Set<XmlConcept> concepts)
	{
		_concepts = concepts;
	}

	Set<XmlConstraint> getConstraints()
	{
		return _constraints;
	}
	
	@XmlElement(name="constraint")
	void setConstraints(Set<XmlConstraint> constraints)
	{
		_constraints = constraints;
	}
	
	Set<XmlGroup> getGroups()
	{
		return _groups;
	}
	
	@XmlElement(name="group")
	void setGroups(Set<XmlGroup> groups)
	{
		_groups = groups;
	}
}
