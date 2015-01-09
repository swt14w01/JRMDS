package jrmds.xml.Model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Constraint")
@XmlType(name="",propOrder={"description", "cypher", "severity"})
public class XmlConstraint
	extends XmlBaseElement
{

	
}
