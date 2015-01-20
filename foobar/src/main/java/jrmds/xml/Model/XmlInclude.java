package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 
 * class for building the XmlModel of included Objects
 *
 */
@XmlTransient
public class XmlInclude
	extends XmlRequire
	{
	
	private EnumSeverity _severity;

	/**
	 * empty constructor
	 */
	public XmlInclude()
	{
	}
	
	/**
	 * constructor with parameters
	 * @param refId
	 * @param severity
	 */
	public XmlInclude(String refId, EnumSeverity severity)
	{
		super(refId);
		_severity = severity;
	}
		
	/**
	 * get the severity
	 * @return
	 */
	@XmlAttribute(name="severity")
	public EnumSeverity getSeverity(){
		return _severity;
	}
	
	/**
	 * set the severity
	 * @param severity
	 */
	public void setSeverity(EnumSeverity severity){
		_severity = severity;
	}
}
