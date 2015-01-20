package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * class for building the XmlModel of Concepts, determine the order
 *
 */
@XmlType(name="",propOrder={"requiresConcept", "description", "cypher", "useQueryTemplate", "parameter"})
public class XmlConcept
	extends XmlBaseElement
{
	
	@JsonProperty("parameter")
    private Set<XmlParameter> _params;

	@JsonProperty("useQueryTemplate")
    private XmlRequire _useQueryTemplate;
    
	/**
	 * get parameter
	 * @return
	 */
	public Set<XmlParameter> getParameter()
	{
		return _params;
	}
	
	/**
	 * set parameter
	 * @param params
	 */
	@XmlElement(name="parameter")
	public void setParameter(Set<XmlParameter> params)
	{
		_params = params;
	}

	/**
	 * get required concepts
	 * @return
	 */
	public XmlRequire getUseQueryTemplate()
	{
		return _useQueryTemplate;
	}
	
	/**
	 * set required concepts
	 * @param requiresConcept
	 */
	@XmlElement(name="useQueryTemplate")
	public void setUseQueryTemplate(XmlRequire useQueryTemplate)
	{
		_useQueryTemplate = useQueryTemplate;
	}
	

	
}
