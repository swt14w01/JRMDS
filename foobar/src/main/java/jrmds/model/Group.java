package jrmds.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Group extends Component {
	private Set<String> optseverity;
	
	public Group() {
		//empty
	}
	
	public Group (String refID) {
		super(refID, ComponentType.GROUP);
	}
	
	public Group (Component component) {
		super(component);
		this.optseverity = component.getGroupSeverity();
	}
	
	public Set<String> getGroupSeverity() {
		if(this.optseverity == null) new HashSet<String>();
		return this.optseverity;
	}
	
	public Map<Integer, String> getOptSeverity(){
		/**
		 * return a Map of the Database IDs (getId():Long) and associated severity for this group 
		 */
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
	
	public void addReference(Component component) {
		super.addReference(component);
	}
	
	public void addReference(Component component, String severity){ //If the Component comes with a severity
		if(optseverity == null) optseverity = new HashSet<String>();
		if(severity==null) throw new NullPointerException("The severity you want to add to the Component is null!");
		if(component==null) throw new NullPointerException("The Component you want to add a reference to is null!");
		//If Component is not a Constraint or Concept, but s.o. wants to add a severity though, it is not possible
		if((component.getType()==ComponentType.CONSTRAINT) || (component.getType()==ComponentType.CONCEPT)){ 
			super.addReference(component); 
			optseverity.add(component.getId().toString() + "-" + severity);
		} else {
			throw new IllegalArgumentException("Cannot add " + component.getType() + " to " + this.getType());
		}
		
	}
	
	public void deleteReference(Component component){
		if(component==null) throw new NullPointerException("The Component on which you want to delete the reference is null!");
		
		//removing the additional Severity just possible, if the component is a Concept or a Constraint
		if(optseverity != null && ( (component.getType()==ComponentType.CONSTRAINT) || (component.getType()==ComponentType.CONCEPT))){
			Set<String> tempSet = new HashSet<>(optseverity);
			Iterator<String> iter = tempSet.iterator();
			while (iter.hasNext()) {
				//walk trough the whole Set and explode every contained string and then compare the Id in the Beginning of the String
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
