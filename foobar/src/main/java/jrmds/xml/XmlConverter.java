package jrmds.xml;

import java.io.InvalidObjectException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import jrmds.model.Component;
import jrmds.model.Concept;
import jrmds.model.Constraint;
import jrmds.model.Group;
import jrmds.xml.Model.EnumSeverity;
import jrmds.xml.Model.XmlConcept;
import jrmds.xml.Model.XmlConstraint;
import jrmds.xml.Model.XmlGroup;
import jrmds.xml.Model.XmlInclude;
import jrmds.xml.Model.XmlRule;
import jrmds.xml.Model.XmlTemplate;

import org.springframework.stereotype.Controller;

@Controller
public class XmlConverter
{
		
	public String objectsToXml(Set<jrmds.model.Component> setComp) throws InvalidObjectException, XmlParseException
	{
		XmlRule rule = GetXmlModelFromJrmdsModel(setComp); 
		return GetXmlFromModel(rule);
	}

	public void objectsToJson(){
		
	}

	public Set<jrmds.model.Component> XmlToObjects(String xmlContent) throws XmlParseException
	{
		try
		{
			XmlRule rule = GetModelFromXml(xmlContent);
			return GetJrmdsModelFromXmlModel(rule);
		}
		catch (JAXBException ex)
		{
			throw new XmlParseException("convertToXml failed: " + ex.getMessage(), ex); 
		}

	}

	public String GetXmlFromModel(XmlRule rule) throws XmlParseException
	{
		try
		{
			JAXBContext jCtx = JAXBContext.newInstance(XmlRule.class);
			Marshaller toXmlMarshaller = jCtx.createMarshaller();
			toXmlMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			toXmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
		    toXmlMarshaller.marshal(rule, sw);

			return sw.toString();
		}
		catch (JAXBException ex)
		{
			throw new XmlParseException("convertToXml failed: " + ex.getMessage(), ex); 
		}
	}


	public XmlRule GetXmlModelFromJrmdsModel(Set<jrmds.model.Component> setComp) throws InvalidObjectException
	{
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
		return rule;
	}

	private XmlRule GetModelFromXml(String xmlContent) throws JAXBException
	{
		JAXBContext jCtx = JAXBContext.newInstance(XmlRule.class);
		Unmarshaller fromXml = jCtx.createUnmarshaller();
		return (XmlRule)fromXml.unmarshal(new StringReader(xmlContent));
	}
	
	private Set<jrmds.model.Component> GetJrmdsModelFromXmlModel(XmlRule rule)
	{
		Set<jrmds.model.Component> setComp = new HashSet<jrmds.model.Component>();
		Map<String, Component> conceptsByRefId = new HashMap<String, Component>();
		Map<String, Component> constraintsByRefId = new HashMap<String, Component>();
		
		for (XmlConcept xc : rule.getConcepts())
		{
			Concept c = XmlConceptToJrmdsConcept(xc);
			setComp.add(c);
			conceptsByRefId.put(c.getRefID(), c);
		}
		
		for (XmlConstraint xc : rule.getConstraints())
		{
			Constraint c = new Constraint();
			c.setCypher(xc.getCypher());
			c.setDescription(xc.getDescription());
			c.setRefID(xc.getId());
			setComp.add(c);
			constraintsByRefId.put(c.getRefID(), c);
		}
		
		for (XmlGroup xg : rule.getGroups()){
			Group g = new Group();
			
			// TODO: wohin mit dem Severity des Includes?
			for (XmlInclude xi : xg.getIncludeConcepts())
			{
				if (conceptsByRefId.containsKey(xi.getRefId()))
				{
					g.addReference(conceptsByRefId.get(xi.getRefId()));
				}
				else
				{
					// TODO: Fall: refId ist nicht im Xml, aber bereits in der DB, was tun? ist das ok so?
					g.addReference(new Concept(xi.getRefId()));
				}
			}
			for (XmlInclude xi : xg.getIncludeConstraints())
			{
				if (constraintsByRefId.containsKey(xi.getRefId()))
				{
					g.addReference(constraintsByRefId.get(xi.getRefId()));
				}
				else
				{
					// TODO: Fall: refId ist nicht im Xml, aber bereits in der DB, was tun? ist das ok so?
					g.addReference(new Constraint(xi.getRefId()));
				}
			}
			for (XmlInclude xi : xg.getIncludeGroups())
			{
				// TODO
			}

			setComp.add(g);
		}
			
		return setComp;
	}
	
	private static Concept XmlConceptToJrmdsConcept(XmlConcept xc)
	{
		Concept c = new Concept();
		c.setCypher(xc.getCypher());
		c.setDescription(xc.getDescription());
		c.setRefID(xc.getId());
		return c;
	}
	
	private static XmlGroup ConvertGroup(jrmds.model.Component group) throws InvalidObjectException
	{
		XmlGroup xg = new XmlGroup();
		xg.setId(group.getRefID());
		xg.setIncludeConcepts(new HashSet<XmlInclude>());
		xg.setIncludeConstraints(new HashSet<XmlInclude>());
		xg.setIncludeGroups(new HashSet<XmlInclude>());
	
		set2group(xg, group.getReferencedComponents());
		
		return xg;
	}
	
	private static XmlConcept ConvertConcept(jrmds.model.Component comp)
	{
		XmlConcept c = new XmlConcept();
		c.setCypher(comp.getCypher());
		c.setId(comp.getRefID());
		c.setDescription(comp.getDescription());
		return c;
	}
	
	private static XmlConstraint ConvertConstraint(Component comp) {
		XmlConstraint c = new XmlConstraint();
		c.setCypher(comp.getCypher());
		c.setId(comp.getRefID());
		c.setDescription(comp.getDescription());
		return c;
	}
	
	private static void set2group(XmlGroup group, Set<jrmds.model.Component> setComp) throws InvalidObjectException
	{
		for (jrmds.model.Component comp : setComp)
		{
			String severity = comp.getSeverity();
			// TODO: theoretisch muss der Wert immer gefüllt sein, aber getSeverity als String kann auch andere Werte haben
			EnumSeverity eSeverity = EnumSeverity.info;
			try
			{
				if (severity != null && !severity.isEmpty())
					eSeverity = EnumSeverity.valueOf(severity);
			}
			catch (Exception ex)
			{
				// irgendwas tun? default = info annehmen
			}
			XmlInclude inc = new XmlInclude(comp.getRefID(), eSeverity);
			switch (comp.getType())
			{
			case CONCEPT:
				group.getIncludeConcepts().add(inc);
				break;
			case CONSTRAINT:
				//add constraint
				group.getIncludeConstraints().add(inc);
				break;
			case GROUP:
				//add group
				group.getIncludeGroups().add(inc);
				break;
			
			default:
				throw new InvalidObjectException("Typ nicht unterstützt");
			}
		}
	}
			
}

