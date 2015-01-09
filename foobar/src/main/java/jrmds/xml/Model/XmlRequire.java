package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;

public class XmlRequire {
	
	private String _refId;
		
	public XmlRequire()
	{
	}
	
	public XmlRequire(String refId)
	{
		_refId = refId;
	}
	
	
	@XmlAttribute(name="refId")
	public String getRefId(){
		return _refId;
	}	
	public void setRefId(String refId){
		_refId = refId;
	}
	
}
