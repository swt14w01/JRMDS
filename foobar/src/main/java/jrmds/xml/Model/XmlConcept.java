package jrmds.xml.Model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;

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
