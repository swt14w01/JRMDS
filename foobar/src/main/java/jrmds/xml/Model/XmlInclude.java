package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;

public class XmlInclude {
	
	private String _refId;
	private EnumSeverity _severity;

	
	public XmlInclude()
	{
	}
	
	public XmlInclude(String refId, EnumSeverity severity)
	{
		_refId = refId;
		_severity = severity;
	}
	
	
	@XmlAttribute(name="refId")
	public String getRefId(){
		return _refId;
	}	
	public void setRefId(String refId){
		_refId = refId;
	}
	
	@XmlAttribute(name="severity")
	public EnumSeverity getSeverity(){
		return _severity;
	}
	public void setSeverity(EnumSeverity severity){
		_severity = severity;
	}
}
