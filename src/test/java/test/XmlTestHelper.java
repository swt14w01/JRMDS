package test;

import java.util.Arrays;

import jrmds.model.Concept;
import jrmds.model.Constraint;
import jrmds.model.Group;

public class XmlTestHelper {

	public static Group CreateGroup(boolean withSubgroup)
	{
		return CreateGroup(withSubgroup, 0);
	}

	private static Group CreateGroup(boolean withSubgroup, int level)
	{
		String levelString = Integer.toString(level);
		Group g = new Group("ref_grp_" + levelString);

		Concept co1 = new Concept("ref_co1_" + levelString);
		co1.setCypher("co_cypher1_" + levelString);
		co1.setDescription("co_desc1_" + levelString);
		Constraint cn1 = new Constraint("ref_cn1_" + levelString);
		cn1.setCypher("cn_cypher1_" + levelString);
		cn1.setDescription("cn_desc1_" + levelString);
		Constraint cn2 = new Constraint("ref_cn2_" + levelString);
		cn2.setCypher("cn_cypher2_" + levelString);
		cn2.setDescription("cn_desc2_" + levelString);
		g.getReferencedComponents().addAll(Arrays.asList(co1, cn1, cn2));

		if (withSubgroup)
		{
			g.getReferencedComponents().add(CreateGroup(level <= 1, level + 1));
			g.getReferencedComponents().add(CreateGroup(false, level + 1));
		}
		
		return g;
	}

}
