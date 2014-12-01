package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jrmds.model.*;

import org.junit.Test;

public class GroupTest {
	Group grp1 = new Group("group1");
	List<String> taglist = new ArrayList<String>();
	
	@Test
	public void CheckConstructor(){ 
		assertEquals("The RefID is wrong!", "group1", grp1.getRefID());
		assertEquals("The Type is wrong!", ComponentType.GROUP, grp1.getType());
		assertEquals("The Tags should be null!", taglist, grp1.getTags());	
		}
	
	@Test
	public void CheckSecondConstructor(){
		taglist.add("Tag1");
		grp1.setTags(taglist);
		Group grp2 = new Group(grp1);
		assertEquals("The RefID is wrong!", "group1", grp2.getRefID());
		assertEquals("The Type is wrong!", ComponentType.GROUP, grp2.getType());
		assertEquals("The Tags are wrong!", taglist, grp1.getTags());
	}
}
