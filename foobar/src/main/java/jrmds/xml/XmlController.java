package jrmds.xml;

import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;





import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jrmds.main.*;
import jrmds.model.Component;
import jrmds.model.Concept;
import jrmds.model.Group;
import jrmds.model.Project;
import jrmds.model.Rule;
import jrmds.xml.*;


public class XmlController {
		
	private XmlValidator xmlvalidate = new XmlValidator();
	
	public boolean validateFile(File localFile){
		return false;
		
	}
	
	public boolean validateUrl(String urlFile){
		
		Boolean response = true;
		try {
			response = xmlvalidate.validateUrl(urlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	public void searchForDuplicates(Project project, Set<String> anotherexternalrepo){
		Set<String> externalrepo = project.getExternalRepos();
	
		for(String extern : externalrepo){
			for (String anotherextern :anotherexternalrepo){
				if (extern.equals(anotherextern)) {System.out.println("Doppelte URL:"+ anotherextern +"!");}
			}	
		}
	}
	
	public void objectsToXml(Set<jrmds.model.Component> setComp) throws InvalidObjectException, JAXBException{
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
				//add constraint
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
	

	private XmlGroup ConvertGroup(jrmds.model.Component group) throws InvalidObjectException
	{
		XmlGroup xg = new XmlGroup();
		xg.setConcepts(new HashSet<XmlConcept>());
		xg.setConstraints(new HashSet<XmlConstraint>());
		xg.setGroups(new HashSet<XmlGroup>());

		set2group(xg, ((Group)group).getReferencedComponents());
		return xg;
	}
	
	private XmlConcept ConvertConcept(jrmds.model.Component comp)
	{
		XmlConcept c = new XmlConcept();
		c.setCypher(comp.getCypher());
		c.setId(comp.getRefID());	// TODO: getId()? Oder fehlt sogar was?
		c.setDescription(comp.getDescription());
		return c;
	}

	private XmlConstraint ConvertConstraint(Component comp) {
		XmlConstraint c = new XmlConstraint();
		c.setCypher(comp.getCypher());
		c.setId(comp.getRefID());	// TODO: getId()? Oder fehlt sogar was?
		c.setDescription(comp.getDescription());
		return c;
	}

 	private void set2group(XmlGroup group, Set<jrmds.model.Component> setComp) throws InvalidObjectException
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
