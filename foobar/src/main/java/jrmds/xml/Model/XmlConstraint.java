package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * class for building the XmlModel of constraints, determine the order
 *
 */
@XmlRootElement(name="Constraint")
@XmlType(name="",propOrder={"description", "cypher", "severity"})
public class XmlConstraint
	extends XmlBaseElement
{

	
}
