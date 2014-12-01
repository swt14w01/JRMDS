package test;

import static org.junit.Assert.*;

import jrmds.model.*;

import org.junit.Test;

public class QueryTemplateTest {
	QueryTemplate qt1 = new QueryTemplate("querytemplate1");

	@Test
	public void CheckConstructor(){ 
		assertEquals("The RefID is wrong!", "querytemplate1", qt1.getRefID());
		assertEquals("The Type is wrong!", ComponentType.TEMPLATE, qt1.getType());
	}
	
	@Test
	public void CheckSecondConstructor(){
		QueryTemplate qt2 = new QueryTemplate(qt1);
		assertEquals("The RefID is wrong!", "querytemplate1", qt2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.TEMPLATE, qt2.getType());
	}
}