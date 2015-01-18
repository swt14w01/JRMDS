package jrmds.xml;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import jrmds.model.Component;
import jrmds.model.ComponentType;
import jrmds.model.Concept;
import jrmds.model.Constraint;
import jrmds.model.Group;
import jrmds.xml.Model.EnumSeverity;
import jrmds.xml.Model.XmlConcept;
import jrmds.xml.Model.XmlConstraint;
import jrmds.xml.Model.XmlGroup;
import jrmds.xml.Model.XmlInclude;
import jrmds.xml.Model.XmlRequire;
import jrmds.xml.Model.XmlRule;
import jrmds.xml.Model.XmlTemplate;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;

import org.springframework.stereotype.Controller;

/**
 * 
 *suppress warning restriction - relative to usage of discouraged or forbidden references
 *class XmlConverter convert from Model or JSON to Xml and from Xml to a Model
 *
 */
@SuppressWarnings("restriction")
@Controller
public class XmlConverter
{
/**
 * Set of Components were given and transformed in a XmlRule, then the funktion to get a Xml as a string from a Model with the XmlRule parameter were called
 * @param setComp
 * @return
 * @throws InvalidObjectException
 * @throws XmlParseException
 */
	public String objectsToXml(Set<jrmds.model.Component> setComp) throws InvalidObjectException, XmlParseException
	{
		XmlRule rule = GetXmlModelFromJrmdsModel(setComp); 
		return GetXmlFromModel(rule);
	}

/**
 * A string with Xml content were given and transformed in a Model as a set of Components
 * @param xmlContent
 * @return
 * @throws XmlParseException
 */
	public Set<jrmds.model.Component> XmlToObjects(String xmlContent) throws XmlParseException
	{
		try
		{
			XmlRule rule = GetModelFromXml(xmlContent);
			validateReferences(rule);
			return GetJrmdsModelFromXmlModel(rule);
		}
		catch (JAXBException ex)
		{
			throw new XmlParseException("convertToXml failed: " + ex.getMessage(), ex); 
		}

	}

	 private void validateReferences(XmlRule rule) throws XmlParseException {
		// list all elements with type
		Map<String, ComponentType> refIdList = new HashMap<String, ComponentType>();
		if (rule.getGroups() != null)
			for (XmlGroup item : rule.getGroups())
				refIdList.put(item.getId(), ComponentType.GROUP);
		if (rule.getConcepts() != null)
			for (XmlConcept item : rule.getConcepts())
				refIdList.put(item.getId(), ComponentType.CONCEPT);
		if (rule.getConstraints() != null)
			for (XmlConstraint item : rule.getConstraints())
				refIdList.put(item.getId(), ComponentType.CONSTRAINT);
		// currently not implemented
		// for (XmlTemplate item : rule.getTemplates())
		// refIdList.put(item.getId(), ComponentType.TEMPLATE);

		// pass every component and test for non-existing links
		if (rule.getGroups() != null)
			for (XmlGroup item : rule.getGroups()) {
				validateReferencesTestGroup(item.getId(), refIdList,
						item.getIncludeConcepts(), ComponentType.CONCEPT);
				validateReferencesTestGroup(item.getId(), refIdList,
						item.getIncludeConstraints(), ComponentType.CONSTRAINT);
				validateReferencesTestGroup(item.getId(), refIdList,
						item.getIncludeGroups(), ComponentType.GROUP);
			}

		if (rule.getConcepts() != null)
			for (XmlConcept item : rule.getConcepts())
				if (item.getRequiresConcept() != null)
					for (XmlRequire req : item.getRequiresConcept())
						validateReferencesThrowExceptionIdNotExists(
								item.getId(), req.getRefId(), refIdList,
								ComponentType.CONCEPT);

		if (rule.getConstraints() != null)
			for (XmlConstraint item : rule.getConstraints())
				if (item.getRequiresConcept() != null)
					for (XmlRequire req : item.getRequiresConcept())
						validateReferencesThrowExceptionIdNotExists(
								item.getId(), req.getRefId(), refIdList,
								ComponentType.CONCEPT);
	}

	void validateReferencesThrowExceptionIdNotExists(String itemId,
			String refId, Map<String, ComponentType> validComponents,
			ComponentType expectedType) throws XmlParseException {
		if (!validComponents.containsKey(refId))
			throw new XmlParseException(String.format(
					"Reference on group \"%s\" to %s \"%s\" not found!",
					itemId, expectedType, refId));
		if (validComponents.get(refId) != expectedType)
			throw new XmlParseException(
					String.format(
							"Reference on group \"%s\" with id \"%s\" shoud be %s, but is \"%s\"!",
							expectedType, itemId, refId, expectedType,
							validComponents.get(refId).toString()));
	}

	void validateReferencesTestGroup(String itemId,
			Map<String, ComponentType> validComponents,
			Set<XmlInclude> referenceList, ComponentType expectedType)
			throws XmlParseException {
		if (referenceList != null)
			for (XmlInclude inc : referenceList)
				validateReferencesThrowExceptionIdNotExists(itemId,
						inc.getRefId(), validComponents, expectedType);
	}

/**
 * A XmlRule were given and transformed in a XML as a String
 * @param rule
 * @return
 * @throws XmlParseException
 */
	private String GetXmlFromModel(XmlRule rule) throws XmlParseException
	{
		try
		{
			JAXBContext jCtx = JAXBContext.newInstance(XmlRule.class);
			Marshaller toXmlMarshaller = jCtx.createMarshaller();
			toXmlMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			toXmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			toXmlMarshaller.setProperty("com.sun.xml.internal.bind.characterEscapeHandler",
	                new CharacterEscapeHandler() {
	                    @Override
	                    public void escape(char[] ch, int start, int length,
	                            boolean isAttVal, Writer writer)
	                            throws IOException {
	                        writer.write(ch, start, length);
	                    }
	                });
			StringWriter sw = new StringWriter();
		    toXmlMarshaller.marshal(rule, sw);

			return sw.toString();
		}
		catch (JAXBException ex)
		{
			throw new XmlParseException("convertToXml failed: " + ex.getMessage(), ex); 
		}
	}
	
/**
 * A Set of Components were given and were transformed in a XmlRule like the Xml Model
 * @param setComp
 * @return
 * @throws InvalidObjectException
 */
	private XmlRule GetXmlModelFromJrmdsModel(Set<jrmds.model.Component> setComp) throws InvalidObjectException
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

	/**
	 * Get a XmlRule from a Xml Content as String
	 * @param xmlContent
	 * @return
	 * @throws JAXBException
	 */
	private XmlRule GetModelFromXml(String xmlContent) throws JAXBException
	{
		JAXBContext jCtx = JAXBContext.newInstance(XmlRule.class);
		Unmarshaller fromXml = jCtx.createUnmarshaller();
		return (XmlRule)fromXml.unmarshal(new StringReader(xmlContent));
	}
	
	/**
	 * A XmlRule were given and you get a Set of Components as a Jrmds Model
	 * @param rule
	 * @return
	 */
	private Set<jrmds.model.Component> GetJrmdsModelFromXmlModel(XmlRule rule)
	{
		Set<jrmds.model.Component> setComp = new HashSet<jrmds.model.Component>();
		Map<String, Component> conceptsByRefId = new HashMap<String, Component>();
		Map<String, Component> constraintsByRefId = new HashMap<String, Component>();
		Map<String, Component> groupsByRefId = new HashMap<String, Component>();
	
		if (rule.getConcepts() != null)
		{
			for (XmlConcept xc : rule.getConcepts())
			{
				Concept c = XmlConceptToJrmdsConcept(xc);
				setComp.add(c);
				conceptsByRefId.put(c.getRefID(), c);
			}
		}
		
		if (rule.getConstraints() != null)
		{
			for (XmlConstraint xc : rule.getConstraints())
			{
				Constraint c = new Constraint(xc.getId());
				c.setCypher(xc.getCypher());
				c.setDescription(xc.getDescription());
				setComp.add(c);
				constraintsByRefId.put(c.getRefID(), c);
			}
		}
		
		if (rule.getGroups() != null)
		{
			for (XmlGroup xg : rule.getGroups()){
				Group g = new Group(xg.getId());
				
				if (xg.getIncludeConcepts() != null)
				{
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
				}
				if (xg.getIncludeConstraints() != null)
				{
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
				}
				if (xg.getIncludeGroups() != null)
				{
					for (XmlInclude xi : xg.getIncludeGroups())
					{
						////////////////////////////////// unsicher ////////////////////////////
						if (groupsByRefId.containsKey(xi.getRefId()))
						{
							g.addReference(groupsByRefId.get(xi.getRefId()));
						}
						else
						{
							// TODO: Fall: refId ist nicht im Xml, aber bereits in der DB, was tun? ist das ok so?
							g.addReference(new Group(xi.getRefId()));
						}
					}
				}
	
				setComp.add(g);
			}
		}
			
		return setComp;
	}
	
	/**
	 * Transform a Concept from XML in a Modelconform Concept
	 * @param xc
	 * @return
	 */
	private static Concept XmlConceptToJrmdsConcept(XmlConcept xc)
	{
		Concept c = new Concept(xc.getId());
		c.setCypher(xc.getCypher());
		c.setDescription(xc.getDescription());
		
		if (xc.getRequiresConcept() != null && xc.getRequiresConcept().size() > 0)
		{
			for (XmlRequire req : xc.getRequiresConcept())
				c.getReferencedComponents().add(new Concept(req.getRefId()));
		}
		
		return c;
	}
	
	/**
	 * Transform a Group from XML in a Modelconform Group
	 * @param group
	 * @return
	 * @throws InvalidObjectException
	 */
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
	
	/**
	 * Transform a Model Concept in a Concept from XML 
	 * @param comp
	 * @return
	 */
	private static XmlConcept ConvertConcept(jrmds.model.Component comp)
	{
		XmlConcept c = new XmlConcept();
		c.setCypher(comp.getCypher());
		c.setId(comp.getRefID());
		c.setDescription(comp.getDescription());
		c.setSeverity(comp.getSeverity());

		c.setRequiresConcept(new HashSet<XmlRequire>());
		for (Component rComp : comp.getReferencedComponents())
			c.getRequiresConcept().add(new XmlRequire(rComp.getRefID()));

		return c;
	}

	/**
	 * Transform a Model Constraint in a Constraint from XML
	 * @param comp
	 * @return
	 */
	private static XmlConstraint ConvertConstraint(Component comp) {
		XmlConstraint c = new XmlConstraint();
		c.setCypher(comp.getCypher());
		c.setId(comp.getRefID());
		c.setDescription(comp.getDescription());
		return c;
	}
	
	/**
	 * transform String in xsd-conform Enum  
	 * @param sev
	 * @return
	 */
	private static EnumSeverity stringToEnum(String sev){
		// TODO: theoretisch muss der Wert immer gefüllt sein, aber getSeverity als String kann auch andere Werte haben
		EnumSeverity eSeverity = EnumSeverity.info;
		try
		{
			if (sev != null && !sev.isEmpty())
				eSeverity = EnumSeverity.valueOf(sev);
		}
		catch (Exception ex)
		{
		}
		return eSeverity;
	}
	
	/**
	 * A Set of Component were divided in Group Objects of a XmlGroup
	 * @param group
	 * @param setComp
	 * @throws InvalidObjectException
	 */
	private static void set2group(XmlGroup group, Set<jrmds.model.Component> setComp) throws InvalidObjectException
	{
		for (jrmds.model.Component comp : setComp)
		{
			String severity = comp.getSeverity();
			EnumSeverity eSeverity = stringToEnum(severity);
			
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
				inc.setSeverity(null);
				group.getIncludeGroups().add(inc);
				break;
			
			default:
				throw new InvalidObjectException("Typ nicht unterstützt");
			}
		}
	}
			
}
