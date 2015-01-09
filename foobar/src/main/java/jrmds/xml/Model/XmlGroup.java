package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class XmlGroup
	extends XmlBaseIdElement {

	@JsonProperty("includeConcept")
    private Set<XmlInclude> _includeConcepts;
    
	@JsonProperty("includeConstraint")
	private Set<XmlInclude> _includeConstraints;
	
	@JsonProperty("group")
	private Set<XmlInclude> _includeGroups;
    
	

	
	@XmlElement(name="includeConstraint")
	public void setIncludeConstraints(Set<XmlInclude> includeConstraints)
	{
		_includeConstraints = includeConstraints;
	}

	public Set<XmlInclude> getIncludeConstraints()
	{
		return _includeConstraints;
	}
	
	public Set<XmlInclude> getIncludeConcepts()
	{
		return _includeConcepts;
	}
	
	@XmlElement(name="includeConcept")
	public void setIncludeConcepts(Set<XmlInclude> includeConcepts)
	{
		_includeConcepts = includeConcepts;
	}

	public Set<XmlInclude> getIncludeGroups()
	{
		return _includeGroups;
	}
	
	@XmlElement(name="includeGroup")
	public void setIncludeGroups(Set<XmlInclude> includeGroups)
	{
		_includeGroups = includeGroups;
	}
}
