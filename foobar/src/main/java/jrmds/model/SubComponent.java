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
		return description;
	}

	public String getCypher() {
		return cypher;
	}

	public Set<Parameter> getParameters() {
		return parameters;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public void setCypher(String cypher) {
		this.cypher = cypher;
	}

	public void setParameters(Set<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void addParameter(Parameter parameter) {
		parameters.add(parameter);
	}

	public void deleteParameter(Parameter para) {
		Set<Parameter> tempSet = new HashSet<>(this.parameters);
		Iterator<Parameter> iter = tempSet.iterator();
		while (iter.hasNext()) {
			Parameter temp = iter.next();
			if (temp.getName().equals(para.getName())) this.parameters.remove(temp);
		}
	}
}
