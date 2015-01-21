package jrmds.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
	/** The Id of the Component for the GraphDatabase.*/
	@GraphId
	private Long id;
	/**The Id of the Component, to identify it in other methods.*/
	private String refID;
	/**The Tags are keywords for the Component, used in search or sorting methods:*/
	private List<String> Tags;
	/**This is the type of the Component. It can be: Group, Constraint, Template, Concept.*/
	private ComponentType type;
	/**A Set of Components on which the current Component depends on.*/
	@RelatedTo(type = "DEPENDSON", direction = Direction.OUTGOING)
	private @Fetch Set<Component> dependsOn;
	/** An optional Severity for groups */
	protected @Fetch Set<String> optseverity;
	/**The external Repositories for groups */
	protected @Fetch Set<String> externalRepos;
	/** this is a hack, to ensure customer satisfaction */
	private String groupSeverityOverwrite;

	/** Empty for Hibernate */
	public Component() {
	}

	/**
	 * This constructor creates a Component object with the given parameters.
	 * @param refID
	 * @param type The Type of the Component object.
	 */
	public Component(String refID, ComponentType type) {
		this.refID = refID;
		this.type = type;
		this.Tags = new ArrayList<String>();
		this.dependsOn = new HashSet<Component>();
		this.optseverity = new HashSet<String>();
		this.externalRepos = new HashSet<String>();
	}
	
	/**
	 * This constructor creates a new Component object with the given Component.
	 * @param component  The given Component object which is used to create a new one.
	 */
	public Component(Component component) {
		this.refID = component.getRefID();
		this.type = component.getType();
		this.Tags = component.getTags();
		this.dependsOn = component.getReferencedComponents();
		this.id = component.getId();
		this.optseverity = component.getGroupSeverity();
		this.externalRepos = component.getExternalRepos();
	}

	/**
	 *Gets the current id of the Component object.
	 *@return id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 *Sets the current id with given id.
	 *@param id  The id which is used to overwrite the current id of the Component object.
	 */
	public void setId(Long id){
		if(id == null) throw new NullPointerException("The id you want to set for this Component is null!");
		this.id = id;
	}

	/**
	 * Gets the current refID of the Component object.
	 * @return refID or "" if the current refID is null.
	 */
	public String getRefID() {
		if(refID == null) return "";
		return refID;
	}

	/**
	 * Sets the current refID with the given refID of the Component object.
	 * @param refID  The refID which is used to overwrite the current refID of the Component.
	 * @throws NullPointerException if the given refID is null.
	 */
	public void setRefID(String refID) {
		if(refID == null) throw new NullPointerException("The refID you want to set for this Component is null!");
		this.refID = refID;
	}

	/**
	 * Gets the current type of the Component object.
	 * @return type 
	 */
	public ComponentType getType() {
		return type;
	}

	/**
	 * Sets the current type with the given type of the Component object.
	 * @param type  The type which is used to overwrite the current type of the Component object.
	 * @throws NullPointerException if the given type is null.
	 */
	public void setType(ComponentType type) {
		if(type == null) throw new NullPointerException("The type you want to set for this Component is null!");
		this.type = type;
	}

	/**
	 * Gets the current Tags of the Component object.
	 * @return Tags or new ArrayList<String> if the current Tags is null.
	 */
	public List<String> getTags() {
		if (Tags == null) return new ArrayList<String>();
		return Tags;
	}

	/**
	 * Adds a given Tag to the current Tags of the Component object.
	 * @param Tag  The given Tag which is to be added to the ArrayList of Tags.
	 * @throws NullPointerException if Tag is null.
	 */
	public void addTag(String Tag) {
		if(Tag == null) throw new NullPointerException("The Tag you want to add to this Component is null!");
		this.Tags.add(Tag);
	}

	/**
	 * Sets the current Tags of the Component object.
	 * @param Tags  The list of Tags which is to be used to overwrite the current list of Tags.
	 * @throws NullPointerException if Tags is null.
	 */
	public void setTags(List<String> Tags) {
		if(Tags == null) throw new NullPointerException("The Tags you want to set for this Component are null!");
		this.Tags = Tags;
	}

	/**
	 * Deletes one Tag in the current ArrayList Tags if the Component object.
	 * @param Tag  The Tag which is to be deleted in the current Tags.
	 * @throws NullPointerException if Tag is null.
	 */
	public void deleteTag(String Tag) {
		if(Tag == null) throw new NullPointerException("The Tag you want to delete in this Component is null!");
		this.Tags.remove(Tag);
	}

	/**
	 * Adds a Reference to another Component object to the current Component object. The other Component is added to the Set dependsOn.
	 * @param cmpt  The Component object which is to be added to the current Set dependsOn.
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
		if (this.type == ComponentType.TEMPLATE) throw new IllegalArgumentException("Cannot add "+ cmpt.getType() +"  to TEMPLATE.");
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
	 * Gets the current Set of Components dependsOn.
	 * @return dependsOn
	 */
	public Set<Component> getReferencedComponents() {
		if (dependsOn == null) return new HashSet<Component>();
		return dependsOn;
	}

	/**
	 * Deletes a Component object of the current Set dependsOn.
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
	 * Copies the values of the Component cmpt into the current Component object.
	 * @param cmpt  The Component object which is to be copied.
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
		this.optseverity = cmpt.getGroupSeverity();
		this.externalRepos = cmpt.getExternalRepos();
	}

	/**
	 * Adds an External Repository to the Component.
	 * @param extRepo external Repository which is to be added.
	 */
	public void addExternalRepo(String extRepo) {
		if(externalRepos == null) externalRepos = new HashSet<String>();
		externalRepos.add(extRepo);
	}

	/**
	 * Adds a Set of external Repositories in the Component.
	 * @param extRepos
	 * @throws NullPointerException if the extRepos is null.
	 */
	public void setExternalRepo(Set<String> extRepos) {
		if (extRepos == null) throw new NullPointerException("The external Repository you want to add must not be null!");
		if(externalRepos == null) externalRepos = new HashSet<String>();
		externalRepos = extRepos;
	}

	/**
	 * Deletes an external Repository out of the Component.
	 * @param extRepo  The external Repository which is to be deleted.
	 * @return true if removed, false if not
	 * @throws NullPointerException if the extRepos is null.
	 */
	public boolean deleteExternalRepo(String extRepo) {
		if (extRepo == null) throw new NullPointerException("You are trying to delete a Repository, which is null!!");
		if(externalRepos == null) externalRepos = new HashSet<String>();
		return externalRepos.remove(extRepo);
	}
	
	/**
	 * Gets the optSeverity of the Component.
	 * @return tempMap  A Map of the optional Severity.
	 */
	public Map<Integer, String> getOptSeverity(){
		Map<Integer,String> tempMap = new HashMap<>();
		if (this.optseverity != null) {
			Iterator<String> iter = this.optseverity.iterator();
			while (iter.hasNext()) {
				String[] exploded = iter.next().split("-");
				Integer l = new Integer(exploded[0]);
				if (exploded.length>1) tempMap.put(l, exploded[1]);
			}
		}
		return tempMap;
	}
	
	/**
	 * Gets the optSeverity of a Group
	 * @return optseverity
	 */
	public Set<String> getGroupSeverity() {
		if(this.optseverity == null) return new HashSet<String>();
		return this.optseverity;
	}
	
	/**
	 * Gets the externalRepos of the Component.
	 * @return externalRepo
	 */
	public Set<String> getExternalRepos() {
		if(this.externalRepos == null) return new HashSet<String>();
		return this.externalRepos;
	}

	/**
	 * Gets groupSeverityOverwrite
	 * @return groupSeverityOverwrite
	 */
	public String getOverwriteSeverity() {
		if (groupSeverityOverwrite==null) return "info";
		return groupSeverityOverwrite;
	}

	/**
	 * Sets groupSeverityOverwrite with to sev.
	 * @param sev  The new value for groupSeverityOverwrite 
	 */
	public void setOverwriteSeverity(String sev) {
		switch (sev) {
		case "blocker": break;
		case "critical": break;
		case "major": break;
		case "minor": break;
		case "info": break;
		default: //throw new IllegalArgumentException("Severity is wrong ");
		}
		this.groupSeverityOverwrite = sev;
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