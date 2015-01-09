package jrmds.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.springframework.stereotype.Controller;
/*
 * This class handles different parameters to get an XML document from an url (extern Repository)
 */
@Controller
public class ExternalRepoRepository {

	/*
	 * Url Input with parameter of string
	 * @Return a Xml document
	 */
	public String GetXmlContentFromUrl(String url) throws XmlParseException
	{
		try
		{
			return GetXmlContentFromUrl(new URL(url));
		}
		catch (MalformedURLException ex)
		{
			throw new XmlParseException("invalid URL", ex);
		}
	}
	
	/*
	 * Url Input with parameter of URL
	 * @Return a Xml document
	 */
	public String GetXmlContentFromUrl(URL url) throws XmlParseException
	{
		try
		{
			try (InputStream iStream = url.openStream())
			{
				try (Scanner s = new Scanner(iStream, "UTF-8"))
				{
					return s.useDelimiter("\\A").next();
				}
			}
		}
		catch (IOException ex)
		{
			throw new XmlParseException("cannot read from URL", ex);
		}
	}
	
}
