package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 
 *Class for building the base elements of the objects 
 *
 */
@XmlTransient
public abstract class XmlBaseElement 
	extends XmlBaseIdElement {

	@JsonProperty("description")
	private String _description;
	@JsonProperty("cypher")
	private String _cypher;
	@JsonProperty("requiresConcept")
    private Set<XmlRequire> _requiresConcept;
	
	/**
	 * get required concepts
	 * @return
	 */
	public Set<XmlRequire> getRequiresConcept()
	{
		return _requiresConcept;
	}
	
	/**
	 * set required concepts
	 * @param requiresConcept
	 */
	@XmlElement(name="requiresConcept")
	public void setRequiresConcept(Set<XmlRequire> requiresConcept)
	{
		_requiresConcept = requiresConcept;
	}
	

	/**
	 * get the description
	 * @return
	 */
	public String getDescription()
	{
		return _description;
	}
	
	/**
	 * set the description
	 * @param description
	 */
	@XmlElement(name="description")
	public void setDescription(String description)
	{
		_description = description;
	}
	
	/**
	 * get the cypher element including the CDATAAdapter class
	 * @return
	 */
	@XmlElement(name ="cypher")
	@XmlJavaTypeAdapter(value=CDATAAdapter.class)
    public String getCypher() {
        return _cypher;
    }

	/**
	 * set the cypher
	 * @param cypher
	 */
    public void setCypher(String cypher) {
        this._cypher = cypher;
    }


	
	@JsonProperty("severity")
	private String _severity;

	/**
	 * get the severity	
	 * @return
	 */
	public String getSeverity(){
		return _severity;
	}
	
	/**
	 * set the severity
	 * @param severity
	 */
	@XmlAttribute(name="severity")
	public void setSeverity(String severity){
		_severity = severity;
	}

}
