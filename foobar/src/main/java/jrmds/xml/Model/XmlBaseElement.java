package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlTransient
public abstract class XmlBaseElement 
	extends XmlBaseIdElement {

	@JsonProperty("description")
	private String _description;
	@JsonProperty("cypher")
	private String _cypher;

	
	public String getDescription()
	{
		return _description;
	}

	@XmlElement(name="description")
	public void setDescription(String description)
	{
		_description = description;
	}
	
	@XmlElement(name ="cypher")
	@XmlJavaTypeAdapter(value=CDATAAdapter.class)
    public String getCypher() {
        return _cypher;
    }

    public void setCypher(String cypher) {
        this._cypher = cypher;
    }


	
	@JsonProperty("severity")
	private String _severity;

		
	public String getSeverity(){
		return _severity;
	}
	
	@XmlAttribute(name="severity")
	public void setSeverity(String severity){
		_severity = severity;
	}

}
