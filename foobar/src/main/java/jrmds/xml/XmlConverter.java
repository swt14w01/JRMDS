package jrmds.xml;

import java.io.InvalidObjectException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import jrmds.model.Component;
import jrmds.model.Group;
import jrmds.model.Rule;
import jrmds.xml.Model.XmlConcept;
import jrmds.xml.Model.XmlConstraint;
import jrmds.xml.Model.XmlGroup;
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
			// TODO: auslagern in Funktion
		JAXBContext jCtx = JAXBContext.newInstance(XmlRule.class);
		Unmarshaller fromXml = jCtx.createUnmarshaller();
		XmlRule rule = (XmlRule)fromXml.unmarshal(new StringReader(xmlContent));
		
		// TODO: Funktion erstellen analog GetXmlFromModel, nur halt jetzt GetModelFromXml
		return GetModelFromXml(rule);
		}
		catch (JAXBException ex)
		{
			throw new XmlParseException("convertToXml failed: " + ex.getMessage(), ex); 
		}

	}
	
	private Set<jrmds.model.Component> GetModelFromXml(XmlRule rule)
	{
		Set<jrmds.model.Component> setComp = new HashSet<jrmds.model.Component>();
			
			for (XmlGroup xg : rule.getGroups()){
				Group g = new Group();
				setComp.add(g);
			}
				
			return setComp;
		
		
	}
	
	public String GetXmlFromModel(XmlRule rule) throws XmlParseException
	{
		try
		{
			JAXBContext jCtx = JAXBContext.newInstance(XmlRule.class);
			Marshaller toXmlMarshaller = jCtx.createMarshaller();
			OutputStream os = new StringOutputStream(); 
			toXmlMarshaller.marshal(rule, os);
			
			return os.toString();
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

	
	private static XmlGroup ConvertGroup(jrmds.model.Component group) throws InvalidObjectException
	{
		XmlGroup xg = new XmlGroup();
		xg.setId(group.getRefID());
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
			
}
