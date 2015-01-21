package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 
 * class for building the Base Elements including the ID
 *
 */
@XmlTransient
public abstract class XmlBaseIdElement {

	@JsonProperty("id")
	private String _id;

	/**
	 * get the Id
	 * @return
	 */
	public String getId()
	{
		return _id;
	}

	/**
	 * set the id
	 * @param id
	 */
	@XmlAttribute(name="id")
	public void setId(String id)
	{
		_id = id;
	}
	
	
}
