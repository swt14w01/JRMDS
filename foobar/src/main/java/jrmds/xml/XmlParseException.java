package jrmds.xml;

@SuppressWarnings("serial")
public class XmlParseException
	extends Exception {


	public XmlParseException()
	{
		super();
	}
	
	public XmlParseException(String message)
	{
		super(message);
	}
	
	public XmlParseException(Throwable innerEx)
	{
		super(innerEx);
	}
	
	public XmlParseException(String message, Throwable innerEx)
	{
		super(message, innerEx);
	}
	
}
