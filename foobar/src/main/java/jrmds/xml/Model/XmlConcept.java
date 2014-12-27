package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="concept")
public class XmlConcept
	extends XmlBaseElement
{
	
	@JsonProperty("requiresConcept")
    private Set<XmlRequire> _requiresConcept;
		
	public Set<XmlRequire> getRequiresConcept()
	{
		return _requiresConcept;
	}
	
	@XmlElement(name="requiresConcept")
	public void setRequiresConcept(Set<XmlRequire> requiresConcept)
	{
		_requiresConcept = requiresConcept;
	}
	
	
}
