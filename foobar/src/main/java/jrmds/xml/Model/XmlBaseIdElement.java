package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlTransient
public abstract class XmlBaseIdElement {

	@JsonProperty("id")
	private String _id;

	
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
