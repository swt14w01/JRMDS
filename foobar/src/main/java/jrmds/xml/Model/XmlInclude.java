package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 
 * class for building the XmlModel of included Objects
 *
 */
public class XmlInclude {
	
	private String _refId;
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
		_refId = refId;
		_severity = severity;
	}
	
	/**
	 * get Reference Id
	 * @return
	 */
	@XmlAttribute(name="refId")
	public String getRefId(){
		return _refId;
	}	
	
	/**
	 * set Reference Id
	 * @param refId
	 */
	public void setRefId(String refId){
		_refId = refId;
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
