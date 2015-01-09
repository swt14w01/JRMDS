package jrmds.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * This class is a container class for groups, constraints, concepts and templates.
 */

@NodeEntity
public abstract class Component {
	/** The Id of the Component for the GraphDatabase.**/
	@GraphId
	private Long id;
	/**The Id of the Component, to identify it in other methods.**/
	private String refID;
	/**The Tags are keywords for the Component, used in search or sorting methods.**/
	private List<String> Tags;
	/**This is the type of the Component. It can be: Group, Constraint, Template, Concept.**/
	private ComponentType type;
	/**A Set of Components on which the current Component depends on.**/
	@RelatedTo(type = "DEPENDSON", direction = Direction.OUTGOING)
	private @Fetch Set<Component> dependsOn;

	public Component() {
	}

	/**
	 * This constructor creates a Component with the given parameters.
	 * @param refID
	 * @param type The Type of the Component.
	 */
	public Component(String refID, ComponentType type) {
		this.refID = refID;
		this.type = type;
		this.Tags = new ArrayList<String>();
		this.dependsOn = new HashSet<Component>();
	}
	
	/**
	 * This constructor creates a Component with the given Component.
	 * @param component 
	 */
	public Component(Component component) {
		this.refID = component.getRefID();
		this.type = component.getType();
		this.Tags = component.getTags();
		this.dependsOn = component.getReferencedComponents();
		this.id = component.getId();
	}

	/**
	 *Gets the parameter id of the Component.
	 *@return id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 *Sets the parameter id.
	 *@param id  The id which is used to overwrite the current id of the Component.
	 */
	public void setId(Long id){
		if(id == null) throw new NullPointerException("The id you want to set for this Component is null!");
		this.id = id;
	}

	/**
	 * Gets the refID of the Component.
	 * @return refID
	 */
	public String getRefID() {
		if(refID == null) return "";
		return refID;
	}

	/**
	 * Sets the refID of the Component.
	 * @param refID  The refID which is used to overwrite the current refID of the Component.
	 */
	public void setRefID(String refID) {
		if(refID == null) throw new NullPointerException("The refID you want to set for this Component is null!");
		this.refID = refID;
	}

	/**
	 * Gets the type of the Component-
	 * @return type 
	 */
	public ComponentType getType() {
		return type;
	}

	/**
	 * Sets the Type of the Component
	 * @param type  The type which is used to overwrite the current type of the Component.
	 */
	public void setType(ComponentType type) {
		if(type == null) throw new NullPointerException("The type you want to set for this Component is null!");
		this.type = type;
	}

	/**
	 * Gets the Tags of the Component.
	 * @return Tags
	 */
	public List<String> getTags() {
		if (Tags == null) return new ArrayList<String>();
		return Tags;
	}

	/**
	 * Adds a Tag to the List of all Tags of the Component.
	 * @param Tag  
	 * @throws NullPointerException if the parameter Tag is null.
	 */
	public void addTag(String Tag) {
		if(Tag == null) throw new NullPointerException("The Tag you want to add to this Component is null!");
		this.Tags.add(Tag);
	}

	/**
	 * Sets the list of Tags.
	 * @param Tags  The list of Tags which is to be used to overwrite the current list of Tags.
	 * @throws NullPointerException if the parameter Tags is null.
	 */
	public void setTags(List<String> Tags) {
		if(Tags == null) throw new NullPointerException("The Tags you want to set for this Component are null!");
		this.Tags = Tags;
	}

	/**
	 * Deletes one Tag in the list of Tags.
	 * @param Tag  The Tag which is to be deleted in the current list of Tags.
	 */
	public void deleteTag(String Tag) {
		if(Tag == null) throw new NullPointerException("The Tag you want to delete in this Component is null!");
		this.Tags.remove(Tag);
	}

	/**
	 * Adds a Reference to another Component to the current Component. The other Component is added to the Set dependsOn.
	 * @param cmpt  The Component which is to be added to the current Set dependsOn.
	 * @throws NullPointerException if the Component cmpt is null.
	 * @throws DuplicateKeyException if the Component cmpt already exists in the Set dependsOn.
	 */
	public void addReference(Component cmpt) {
		if(cmpt == null) throw new NullPointerException("The Reference you want to add to this Component is null!");
		
		if (this.dependsOn == null)	dependsOn = new HashSet<Component>();
		if (this.dependsOn.contains(cmpt)) throw new DuplicateKeyException("Component " + cmpt.getRefID() + " is already referenced");
		
		/**
		 * Checks if a compatible type of component is added to the Set dependsOn of the current Component.
		 * @throws IllegalArgumentException if the type of cmpt is not compatible as a reference for the current type of Component.
		 */
		if (this.type == ComponentType.TEMPLATE) throw new IllegalArgumentException("Cannot add TEMPLATE to " + this.getType());
		if (this.type == ComponentType.GROUP) {
			if (cmpt.getType() == ComponentType.TEMPLATE) throw new IllegalArgumentException("Cannot add TEMPLATE to " + this.getType());
		}
		
		if (this.type == ComponentType.CONCEPT || this.type == ComponentType.CONSTRAINT) {
			if ((cmpt.getType() == ComponentType.GROUP) || (cmpt.getType()==ComponentType.CONSTRAINT)) throw new IllegalArgumentException("Cannot add " + cmpt.getType() + " to " + this.getType());
			if(cmpt.getType()==ComponentType.TEMPLATE) {
				for(Component template:dependsOn) {
					if (template.getType() == ComponentType.TEMPLATE) throw new IllegalArgumentException("Cannot add " + cmpt.getType() + " to " + this.getType());;
				}
			}
		}
		dependsOn.add(cmpt);
	}
	
	/**
	 * Gets the Set of Components dependsOn.
	 * @return dependsOn
	 */
	public Set<Component> getReferencedComponents() {
		if (dependsOn == null) return new HashSet<Component>();
		return dependsOn;
	}

	/**
	 * Deletes a Component of the current Set of Components dependsOn.
	 * @param cmpt  The Component which is to be deleted in the current Set dependsOn.
	 * @throws NullPointerException if the Component cmpt is null.
	 */
	public void deleteReference(Component cmpt) {
		if(cmpt == null) throw new NullPointerException("The Reference you want to delete in this Component is null!");
		Set<Component> tempSet = new HashSet<>(dependsOn);
		Iterator<Component> iter = tempSet.iterator();
		while (iter.hasNext()) {
			Component temp = iter.next();
			if (cmpt.getRefID().equals(temp.getRefID())) this.dependsOn.remove(temp);
		}
	}

	/**
	 * Copies the values of the Component cmpt into the current Component.
	 * @param cmpt  The Component which is to be copied.
	 * @throws NullPointerException if Component cmpt is null.
	 * @throws IllegalArgumentException if the type of cmpt doesn't match the type of the current Component.
	 */
	public void copy(Component cmpt) {
		if(cmpt == null) throw new NullPointerException("The Component you want to copy is null!");
		if(this.type!=cmpt.getType()) throw new IllegalArgumentException("You can not copy a different Type!");
		this.refID = cmpt.getRefID();
		this.type = cmpt.getType();
		this.Tags = cmpt.getTags();
		this.setCypher(cmpt.getCypher());
		this.setDescription(cmpt.getDescription());
		this.setParameters(cmpt.getParameters());
		this.setSeverity(cmpt.getSeverity());
	}

	/**
	 * Following methods are place holders, so that Component objects can be converted to other objects.
	 */
	public String getDescription() {
		return "NA";
	}

	public String getSeverity() {
		return "NA";
	}
	
	public Set<String> getGroupSeverity() {
		return new HashSet<>();
	}

	public String getCypher() {
		return "NA";
	}

	public Set<Parameter> getParameters() {
		return new HashSet<Parameter>();
	}

	public void setDescription(String desc) {
	}

	public void setCypher(String cypher) {
	}

	public void setParameters(Set<Parameter> para) {
	}

	public void addParameter(Parameter para) {
	}
	
	public void deleteParameter(Parameter para) {
	}

	public void setSeverity(String sev) {
	}
}
