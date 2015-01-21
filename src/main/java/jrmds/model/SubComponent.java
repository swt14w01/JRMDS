package jrmds.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelatedTo;

/** 
 * SubComponents are Rule and QueryTemplates.
 */
public abstract class SubComponent extends Component {
	/** A description for SubComponent objects */
	private String description;
	/**A cypher for SubComponent objects*/
	private String cypher;
	/**This is a Set of Parameter objects*/
	@RelatedTo(type = "PARAMETER", direction = Direction.BOTH)
	private @Fetch Set<Parameter> parameters;

	/** Empty for Hibernate */
	public SubComponent() {
	}

	/**
	 * Constructor to create a new SubComponent object with a refID and a Component type. Gives the refID and the type to the constructor in Component.
	 * @param refID  The given refID for the new SubComponent object.
	 * @param type  The given type for the new SubComponent object.
	 */
	public SubComponent(String refID, ComponentType type) {
		super(refID, type);
		parameters = new HashSet<Parameter>();
	}

	/**
	 * Constructor to create a new SubComponent object with component. Gives the component to the constructor in Component.
	 * @param component  The given Component object to create a new SubComponent object.
	 */
	public SubComponent(Component component) {
		super(component);
		super.setTags(component.getTags());
		description = component.getDescription();
		cypher = component.getCypher();
		parameters = component.getParameters();
	}

	/**
	 * Gets the current description of the SubComponent object.
	 * @return description or "" if the description is null.
	 */
	public String getDescription() {
		if(this.description==null) return "";
		return description;
	}

	/**
	 * Gets the current cypher of the SubComponent object.
	 * @return cypher or "" it the cypher is null.
	 */
	public String getCypher() {
		if(this.cypher==null) return "";
		return cypher;
	}

	/**
	 * Gets the current Set of Parameters of the SubComponent object.
	 * @return parameters or a new HashSet<Parameter> if parameters is null.
	 */
	public Set<Parameter> getParameters() {
		if (parameters == null) return new HashSet<Parameter>();
		return parameters;
	}

	/**
	 * Sets the current description of the cypher with the given description desc.
	 * @param desc  The given description to overwrite the current description.
	 * @throws NullPointerException if desc is null.
	 */
	public void setDescription(String desc) {
		if(desc == null) throw new NullPointerException("The description you want to set for this Component is null!");
		this.description = desc;
	}

	/**
	 * Sets the current cypher with the given cypher.
	 * @param cypher  The given cypher to overwrite the current cypher.
	 * @throws NullPointerException if given cypher is null.
	 */
	public void setCypher(String cypher) {
		if(cypher == null) throw new NullPointerException("The cypher you want to set for this ComPonent is null!");
		this.cypher = cypher;
	}

	/**
	 * Sets the current parameters with the given parameters.
	 * @param parameters  The given parameters to overwrite the current parameters.
	 * @throws NullPointerException if given parameters is null.
	 */
	public void setParameters(Set<Parameter> parameters) {
		if(parameters == null) throw new NullPointerException("The parameters you want to set for this Comoonent is null!");
		this.parameters = parameters;
	}

	/**
	 * Adds a Parameter object to the current Set parameters.
	 * @param parameter  The given parameter which is to be added.
	 * @throws NullPointerException if given parameter is null.
	 */
	public void addParameter(Parameter parameter) {
		if(parameter == null) throw new NullPointerException("The parameter you want to add to this Comoonent is null!");
		
		parameters.add(parameter);
	}

	/**
	 * Deletes a Parameter object of the current Set parameters.
	 * @param para  The given parameter which is to be deleted.
	 * @throws NullPointerException if para is null.
	 */
	public void deleteParameter(Parameter para) {
		if(para == null) throw new NullPointerException("The parameter you want to delete in this Component is null!");
		Set<Parameter> tempSet = new HashSet<>(this.parameters);
		Iterator<Parameter> iter = tempSet.iterator();
		while (iter.hasNext()) {
			Parameter temp = iter.next();
			if (temp.getName().equals(para.getName())) this.parameters.remove(temp);
		}
	}
}
