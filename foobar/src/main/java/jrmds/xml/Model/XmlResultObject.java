package jrmds.xml.Model;

public class XmlResultObject {
	
	boolean _success;
	String _message;
	
	
	public XmlResultObject(boolean success, String message)
	{
		_success = success;
		_message = message;
	}
	

	public boolean getSuccess()
	{
		return _success;
	}
	
	public String getMessage()
	{
		return _message;
	}
	
}
