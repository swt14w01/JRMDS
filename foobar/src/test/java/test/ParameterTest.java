package test;

import static org.junit.Assert.*;

import org.junit.Test;

import jrmds.model.Parameter;

public class ParameterTest {
Parameter parastring = new Parameter("parameter1","parametervalue", true);
Parameter paranotstring = new Parameter("parameter2", "parametervalue", false);
Parameter paranull = new Parameter(null, null, null);
	
	@Test
	public void getNameTest(){
		assertEquals("The method getName() does not work correctly!", "parameter1", parastring.getName());
		assertEquals("The method getName() does not work correctly!", "parameter2", paranotstring.getName());
		assertEquals("The method getName() does not work correctly!", "", paranull.getName());
	}

	
	
	@Test
	public void isStringTest(){
		assertEquals("The method isString() does not work correctly! Should return true when the Parameter is a String!","true",parastring.isString());
		assertEquals("The method isString() does not work correctly! Should return false when the Parameter is not a String!","false",paranotstring.isString());
		assertEquals("The method isString() does not work correctly! Should return false when the Parameter Type is null!", "false", paranull.isString());
	}
	
	@Test
	public void getValueTest(){
		assertEquals("The method getValue() does not work correctly!", "parametervalue", parastring.getValue());
		assertEquals("The method getValue() does not work correctly!", "parametervalue", paranotstring.getValue());
		assertEquals("The method getValue() does not work correctly!", "", paranull.getValue());
	}
	
	@Test
	public void setValueTest(){
		parastring.setValue("paramstring", true);
		paranotstring.setValue("paramnotstring",false);
		assertEquals("The method setValue() does not work correctly!","paramstring", parastring.getValue());
		assertEquals("The method setValue() does not work correctly!","paramnotstring", paranotstring.getValue());
		
		assertEquals("The method setValue() does not work correctly! Should return true when the Parameter is a String!","true",parastring.isString());
		assertEquals("The method setValue() does not work correctly! Should return false when the Parameter is not a String!","false",paranotstring.isString());
	}
	
	@Test(expected = NullPointerException.class)
	public void setValueNullTest(){
		paranotstring.setValue(null,false);
	}
	
	@Test(expected = NullPointerException.class)
	public void setisStringNullTest(){
		paranotstring.setValue("parameter2",null);
	}
}
