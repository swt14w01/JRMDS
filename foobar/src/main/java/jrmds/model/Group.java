package jrmds.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class represents all Group objects.
 */
public class Group extends Component {
	/** The optseverity is an optional severity to overwrite the severity of the referenced components. */
	private Set<String> optseverity;
	
	/** Empty for Hibernate */
	public Group() {
	}
	
	/**
	 * Constructor to create a new Group object with a refID. Gives the refID and the type to the constructor in Component.
	 * @param refID  The given refID for the new Group object.
	 */
	public Group (String refID) {
		super(refID, ComponentType.GROUP);
	}
	
	/**
	 * Constructor to create a new Group object with component. Gives the component to the constructor in Component.
	 * @param component  The given component for the new Group object.
	 */
	public Group (Component component) {
		super(component);
		this.optseverity = component.getGroupSeverity();
	}
	
	/**
	 * Gets the current optseverity of the Group object. If the optseverity is null, an empty HashSet<String> is returned.
	 * @return optseverity
	 */
	public Set<String> getGroupSeverity() {
		if(this.optseverity == null) new HashSet<String>();
		return this.optseverity;
	}
	
	
	/**
	 * Gets a Map of the Database IDs (getId():Long) and associated severity for this group.
	 * @return tempMap
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
	 * Adds a reference to the Set dependsOn of the Group object. Uses the addReference of Component class.
	 * @param component  The component which is to be added as a reference.
	 */
	public void addReference(Component component) {
		super.addReference(component);
	}
	
	/**
	 * Adds a component as a reference to the Set dependsOn with a severity for it. Uses the method addReference of Component class.
	 * @param component  The component which is to be added as a reference.
	 * @param severity  The severity for the component.
	 * @throws NullPointerException if the given component is null or if the given severity is null.
	 * @throws IllegalArgumentException if the type of component is neither a concept nor a constraint.
	 */
	public void addReference(Component component, String severity){ 
		if(optseverity == null) optseverity = new HashSet<String>();
		if(severity==null) throw new NullPointerException("The severity you want to add to the Component is null!");
		if(component==null) throw new NullPointerException("The Component you want to add a reference to is null!");
	/**
	 * The component will be added, if the type is either a constraint or a concept.
	 */
		if((component.getType()==ComponentType.CONSTRAINT) || (component.getType()==ComponentType.CONCEPT)){ 
			super.addReference(component); 
			optseverity.add(component.getId().toString() + "-" + severity);
		} else {
			throw new IllegalArgumentException("Cannot add " + component.getType() + " to " + this.getType());
		}
		
	}
	
	/**
	 * Deletes a Reference out of the Set dependsOn of this Group object. Uses the deleteReference of Component class.
	 * @param component  The component which is to be deleted.
	 * @throws NullPointerException if component is null.
	 */
	public void deleteReference(Component component){
		if(component==null) throw new NullPointerException("The Component on which you want to delete the reference is null!");
		
		/**
		 * Removing the additional Severity just possible, if the component is a Concept or a Constraint.
		 */
		if(optseverity != null && ( (component.getType()==ComponentType.CONSTRAINT) || (component.getType()==ComponentType.CONCEPT))){
			Set<String> tempSet = new HashSet<>(optseverity);
			Iterator<String> iter = tempSet.iterator();
			while (iter.hasNext()) {
				/**
				 * walks trough the whole Set and explores every contained string and then compares the Ids at the beginning of the String.
				 */
				String temp = iter.next();
				String[] exploded = temp.split("-");
				if (exploded[0].equals(component.getId().toString())) {
					optseverity.remove(temp);
				}
			} 
		}
		super.deleteReference(component);
	}
}
