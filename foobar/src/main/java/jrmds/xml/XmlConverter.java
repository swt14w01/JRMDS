package jrmds.xml;


import java.io.File;
import java.io.InvalidObjectException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import jrmds.model.Component;
import jrmds.model.Group;


public class XmlConverter {
	
	
	public void xmlToObjects(){
		
	}
	
	public static void objectsToXml(Set<jrmds.model.Component> setComp) throws InvalidObjectException, JAXBException{
			XmlRule rule = new XmlRule();
			rule.setConcepts(new HashSet<XmlConcept>());
			rule.setConstraints(new HashSet<XmlConstraint>());
			rule.setGroups(new HashSet<XmlGroup>());
			rule.setTemplates(new HashSet<XmlTemplate>());

			for (jrmds.model.Component comp : setComp)
			{
				switch (comp.getType())
				{
				case CONCEPT:
					rule.getConcepts().add(ConvertConcept(comp));
					break;
				case CONSTRAINT:
					rule.getConstraints().add(ConvertConstraint(comp));
					break;
				case GROUP:
					//add group
					rule.getGroups().add(ConvertGroup(comp));
					break;
				case TEMPLATE:
					//add template
					break;

				default:
					throw new InvalidObjectException("Typ nicht unterstützt");
				}
			}
			
			JAXBContext jCtx = JAXBContext.newInstance(jrmds.model.Rule.class);
			Marshaller toXmlMarshaller = jCtx.createMarshaller();
			toXmlMarshaller.marshal(rule, new File("D:\\Rule.xml"));
			
	}

private static XmlGroup ConvertGroup(jrmds.model.Component group) throws InvalidObjectException
{
	XmlGroup xg = new XmlGroup();
	xg.setConcepts(new HashSet<XmlConcept>());
	xg.setConstraints(new HashSet<XmlConstraint>());
	xg.setGroups(new HashSet<XmlGroup>());

	set2group(xg, ((Group)group).getReferencedComponents());
	return xg;
}

private static XmlConcept ConvertConcept(jrmds.model.Component comp)
{
	XmlConcept c = new XmlConcept();
	c.setCypher(comp.getCypher());
	c.setId(comp.getRefID());	// TODO: getId()? Oder fehlt sogar was?
	c.setDescription(comp.getDescription());
	return c;
}

private static XmlConstraint ConvertConstraint(Component comp) {
	XmlConstraint c = new XmlConstraint();
	c.setCypher(comp.getCypher());
	c.setId(comp.getRefID());	// TODO: getId()? Oder fehlt sogar was?
	c.setDescription(comp.getDescription());
	return c;
}

private static void set2group(XmlGroup group, Set<jrmds.model.Component> setComp) throws InvalidObjectException
{
	for (jrmds.model.Component comp : setComp)
	{
		switch (comp.getType())
		{
		case CONCEPT:
			group.getConcepts().add(ConvertConcept(comp));
			break;
		case CONSTRAINT:
			//add constraint
			group.getConstraints().add(ConvertConstraint(comp));
			break;
		case GROUP:
			//add group
			group.getGroups().add(ConvertGroup(comp));
			break;
		
		default:
			throw new InvalidObjectException("Typ nicht unterstützt");
		}
	}
}

	
	public void objectsToJson(){
		
	}
			
}