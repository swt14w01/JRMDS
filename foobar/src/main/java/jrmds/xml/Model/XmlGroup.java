package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * class for building the XmlModel of Groups
 *
 */
public class XmlGroup
	extends XmlBaseIdElement {

	@JsonProperty("includeConcept")
    private Set<XmlInclude> _includeConcepts;
    
	@JsonProperty("includeConstraint")
	private Set<XmlInclude> _includeConstraints;
	
	@JsonProperty("group")
	private Set<XmlInclude> _includeGroups;
    
	

	/**
	 * set the included constraints
	 * @param includeConstraints
	 */
	@XmlElement(name="includeConstraint")
	public void setIncludeConstraints(Set<XmlInclude> includeConstraints)
	{
		_includeConstraints = includeConstraints;
	}

	/**
	 * get the included constraints
	 * @return
	 */
	public Set<XmlInclude> getIncludeConstraints()
	{
		return _includeConstraints;
	}
	
	/**
	 * get the included concepts
	 * @return
	 */
	public Set<XmlInclude> getIncludeConcepts()
	{
		return _includeConcepts;
	}
	
	/**
	 * set the included concepts
	 * @param includeConcepts
	 */
	@XmlElement(name="includeConcept")
	public void setIncludeConcepts(Set<XmlInclude> includeConcepts)
	{
		_includeConcepts = includeConcepts;
	}

	/**
	 * get the included groups
	 * @return
	 */
	public Set<XmlInclude> getIncludeGroups()
	{
		return _includeGroups;
	}
	
	/**
	 * set the included groups
	 * @param includeGroups
	 */
	@XmlElement(name="includeGroup")
	public void setIncludeGroups(Set<XmlInclude> includeGroups)
	{
		_includeGroups = includeGroups;
	}
}
