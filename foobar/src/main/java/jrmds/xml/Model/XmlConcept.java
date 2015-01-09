package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * class for building the XmlModel of Concepts, determine the order
 *
 */
@XmlRootElement(name="concept")
@XmlType(name="",propOrder={"requiresConcept", "description", "cypher"})
public class XmlConcept
	extends XmlBaseElement
{
	
	
	
}
