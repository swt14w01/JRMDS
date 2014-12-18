package jrmds.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelatedTo;

public abstract class SubComponent extends Component {
	private String description;
	private String cypher;
	@RelatedTo(type = "PARAMETER", direction = Direction.BOTH)
	private @Fetch Set<Parameter> parameters;

	public SubComponent() {
		// empty Constructor for no-arg
	}

	public SubComponent(String refID, ComponentType type) {
		super(refID, type);
		parameters = new HashSet<Parameter>();
	}

	public SubComponent(Component component) {
		super(component);
		super.setTags(component.getTags());
		description = component.getDescription();
		cypher = component.getCypher();
		parameters = component.getParameters();
	}

	public String getDescription() {
		if(this.description==null) throw new NullPointerException("The description of this Component is null!");
		return description;
	}

	public String getCypher() {
		if(this.cypher==null) throw new NullPointerException("The Cypher of this Component is null!");
		return cypher;
	}

	public Set<Parameter> getParameters() {
		if (parameters == null) return new HashSet<Parameter>();
		return parameters;
	}

	public void setDescription(String desc) {
		if(desc == null) throw new NullPointerException("The description you want to set for this Comoonent is null!");
		this.description = desc;
	}

	public void setCypher(String cypher) {
		if(cypher == null) throw new NullPointerException("The cypher you want to set for this Comoonent is null!");
		this.cypher = cypher;
	}

	public void setParameters(Set<Parameter> parameters) {
		if(parameters == null) throw new NullPointerException("The parameters you want to set for this Comoonent is null!");
		this.parameters = parameters;
	}

	public void addParameter(Parameter parameter) {
		if(parameter == null) throw new NullPointerException("The parameter you want to add to this Comoonent is null!");
		
		parameters.add(parameter);
	}

	public void deleteParameter(Parameter para) {
		if(para == null) throw new NullPointerException("The parameter you want to delete in this Comoonent is null!");
		Set<Parameter> tempSet = new HashSet<>(this.parameters);
		Iterator<Parameter> iter = tempSet.iterator();
		while (iter.hasNext()) {
			Parameter temp = iter.next();
			if (temp.getName().equals(para.getName())) this.parameters.remove(temp);
		}
	}
}
