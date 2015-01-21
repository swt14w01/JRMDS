package jrmds.xml.Model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * 
 * class to print out CDATA in the right way in a xml
 *
 */
public class CDATAAdapter extends XmlAdapter<String, String> 
{
	/**
	 * marshalling
	 */
	@Override
	public String marshal(String v) throws Exception
	{
		return "<![CDATA[" + v + "]]>";
	}
	/**
	 * unmarshalling
	 */
	@Override
	public String unmarshal(String v) throws Exception
	{
		return v;
	}

}