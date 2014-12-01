package jrmds.xml;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jrmds.main.*;
import jrmds.model.Rule;

@RestController
public class XmlConverter {
	
	@RequestMapping ("/welcome")
	String welcome(
			@RequestParam(value = "name", defaultValue = "World") String name) throws JAXBException{

		try {
			JAXBContext jCtx = JAXBContext.newInstance(Rule.class);
			Unmarshaller fromXml = jCtx.createUnmarshaller();
			Rule rXml = (Rule)fromXml.unmarshal( new FileReader("D:\\model.xml"));

			ObjectMapper mapper = new ObjectMapper();
	        System.out.println(mapper.writeValueAsString(rXml));
			


			Rule fromJson = mapper.readValue(new FileReader("D:\\model.json"), Rule.class);
			
			Marshaller toXmlMarshaller = jCtx.createMarshaller();
			toXmlMarshaller.marshal(fromJson, System.out);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return "Welcome ".concat(name).concat("!");		
	}
	public void xmlToObjects(){
		
	}
	
	public void objectsToXml(){
		
	}
	
	public void objectsToJson(){
		
	}
			
}