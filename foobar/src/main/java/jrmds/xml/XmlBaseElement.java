package jrmds.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlTransient
public abstract class XmlBaseElement {

	@JsonProperty("id")
	private String _id;
	@JsonProperty("cypher")
	private String _cypher;
	@JsonProperty("description")
	private String _description;
	

	@XmlElement(name ="cypher")
    public String getCypher() {
        return _cypher;
    }

    public void setCypher(String cypher) {
        this._cypher = cypher;
    }

	public String getDescription()
	{
		return _description;
	}

	@XmlElement(name="description")
	public void setDescription(String description)
	{
		_description = description;
	}

	public String getId()
	{
		return _id;
	}

	@XmlAttribute(name="id")
	public void setId(String id)
	{
		_id = id;
	}

}
