package test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import jrmds.model.*;

import org.junit.Test;


public class SubComponentTest {

	SubComponent subctempl1 = new QueryTemplate("subctemplate1");
	SubComponent subcconst1 = new Constraint("subcconstraint1");
	SubComponent subcconc1 = new Concept("subcconcept1");
	Set<Parameter> parameterlist = new HashSet<Parameter>();
	
	@Test
	public void CheckConstructor(){ 
		//Template Check
		assertEquals("The RefID is wrong!", "subctemplate1",subctempl1.getRefID());
		assertEquals("The Type is wrong!", ComponentType.TEMPLATE ,subctempl1.getType());
		assertEquals("The Parameterlist is wrong!", parameterlist, subctempl1.getParameters());
		assertEquals("The Description has to be null!", null, subctempl1.getDescription());
		assertEquals("The Cypher has to be null!", null, subctempl1.getCypher());
		
		//Constraint Check
		assertEquals("The RefID is wrong!", "subcconstraint1",subcconst1.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONSTRAINT ,subcconst1.getType());
		assertEquals("The Parameterlist is wrong!", parameterlist, subcconst1.getParameters());
		assertEquals("The Description has to be null!", null, subcconst1.getDescription());
		assertEquals("The Cypher has to be null!", null, subcconst1.getCypher());
		
		//Concept Check
		assertEquals("The RefID is wrong!", "subcconcept1",subcconc1.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONCEPT ,subcconc1.getType());
		assertEquals("The Parameterlist is wrong!", parameterlist, subcconc1.getParameters());
		assertEquals("The Description has to be null!", null, subcconc1.getDescription());
		assertEquals("The Cypher has to be null!", null, subcconc1.getCypher());
	}
	
	
	@Test
	public void addParameterTest(){
		//getter not testable
		Parameter para1 = new Parameter("one", "two");
		parameterlist.add(para1);
		
		subctempl1.addParameter(para1);
		subcconc1.addParameter(para1);
		subcconst1.addParameter(para1);
		
		assertEquals("Method addParameters() does not work correctly!", parameterlist, subctempl1.getParameters());
		assertEquals("Method addParameters() does not work correctly!", parameterlist, subcconc1.getParameters());
		assertEquals("Method addParameters() does not work correctly!", parameterlist, subcconst1.getParameters());
	}
	
	@Test
	public void setParametersTest(){
		Parameter para1 = new Parameter("one", "two");
		parameterlist.add(para1);
		
		subctempl1.setParameters(parameterlist);
		subcconc1.setParameters(parameterlist);
		subcconst1.setParameters(parameterlist);
		
		assertEquals("Method setParameters() does not work correctly!", parameterlist, subctempl1.getParameters());
		assertEquals("Method setParameters() does not work correctly!", parameterlist, subcconc1.getParameters());
		assertEquals("Method setParameters() does not work correctly!", parameterlist, subcconst1.getParameters());
	}
	
	@Test
	public void deleteParameterTest(){
		Parameter para1 = new Parameter("one", "two");
		parameterlist.add(para1);
		
		subctempl1.setParameters(parameterlist);
		subcconc1.setParameters(parameterlist);
		subcconst1.setParameters(parameterlist);
		
		parameterlist.remove(para1);
		
		subctempl1.deleteParameter(para1);
		subcconst1.deleteParameter(para1);
		subcconc1.deleteParameter(para1);
		
		assertEquals("Method deleteParameter() does not work correctly!", parameterlist, subctempl1.getParameters());
		assertEquals("Method deleteParameter() does not work correctly!", parameterlist, subcconc1.getParameters());
		assertEquals("Method deleteParameter() does not work correctly!", parameterlist, subcconst1.getParameters());
	}

	
	@Test
	public void setCypherTest(){
		//getter not testable
				subctempl1.setCypher("cyph1");
				subcconc1.setCypher("cyph1");
				subcconst1.setCypher("cyph1");
				
				assertEquals("Method setCypher() does not work correctly!", "cyph1", subctempl1.getCypher());
				assertEquals("Method setCypher() does not work correctly!", "cyph1", subcconc1.getCypher());
				assertEquals("Method setCypher() does not work correctly!", "cyph1", subcconst1.getCypher());
	}
	
	@Test
	public void setDescriptionTest(){
		//getter not testable
				subctempl1.setDescription("descr1");
				subcconc1.setDescription("descr1");
				subcconst1.setDescription("descr1");
				
				assertEquals("Method setDescription() does not work correctly!", "descr1", subctempl1.getDescription());
				assertEquals("Method setDescription() does not work correctly!", "descr1", subcconc1.getDescription());
				assertEquals("Method setDescription() does not work correctly!", "descr1", subcconst1.getDescription());
	}
	
	@Test
	public void CheckSecondConstructor(){
		subctempl1.setDescription("descr1");
		subcconc1.setDescription("descr1");
		subcconst1.setDescription("descr1");
		
		subctempl1.setCypher("cyph1");
		subcconc1.setCypher("cyph1");
		subcconst1.setCypher("cyph1");
		
		Parameter para1 = new Parameter("one", "two");
		parameterlist.add(para1);
		
		subctempl1.setParameters(parameterlist);
		subcconc1.setParameters(parameterlist);
		subcconst1.setParameters(parameterlist);
		
		
		SubComponent subctempl2 = new QueryTemplate(subctempl1);
		SubComponent subcconst2 = new Constraint(subcconst1);
		SubComponent subcconc2 = new Concept(subcconc1);
		
		//Template
		assertEquals("The RefID is wrong!", "subctemplate1",subctempl2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.TEMPLATE ,subctempl2.getType());
		assertEquals("The Parameterlist is wrong!", parameterlist, subctempl2.getParameters());
		assertEquals("The Description is wrong!", "descr1", subctempl2.getDescription());
		assertEquals("The Cypher is wrong!", "cyph1", subctempl2.getCypher());
		
		//Concept
		assertEquals("The RefID is wrong!", "subcconcept1",subcconc2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONCEPT ,subcconc2.getType());
		assertEquals("The Parameterlist is wrong!", parameterlist, subcconc2.getParameters());
		assertEquals("The Cypher is wrong!", "cyph1", subcconc2.getCypher());
		assertEquals("The Description is wrong!", "descr1", subcconc2.getDescription());
		
		//Constraint
		assertEquals("The RefID is wrong!", "subcconstraint1",subcconst2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.CONSTRAINT ,subcconst2.getType());
		assertEquals("The Parameterlist is wrong!", parameterlist, subcconst2.getParameters());
		assertEquals("The Cypher is wrong!", "cyph1", subcconst2.getCypher());
		assertEquals("The Description is wrong!", "descr1", subcconst2.getDescription());
	}
}
