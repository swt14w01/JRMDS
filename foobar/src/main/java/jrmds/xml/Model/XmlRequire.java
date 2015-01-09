package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 
 * class to build the required elements as objects
 *
 */
public class XmlRequire {
	
	private String _refId;
	
	/**
	 * empty constructor
	 */
	public XmlRequire()
	{
	}
	
	/**
	 * constructor with parameters
	 * @param refId
	 */
	public XmlRequire(String refId)
	{
		_refId = refId;
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
	 * set Ref Id
	 * @param refId
	 */
	public void setRefId(String refId){
		_refId = refId;
	}
	
}
