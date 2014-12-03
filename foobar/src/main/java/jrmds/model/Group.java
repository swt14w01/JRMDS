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
		super(component.getRefID(), ComponentType.GROUP);
		super.setTags(component.getTags());
	}
	
	public Map<Integer, String> getOptSeverity(){
		/**
		 * return a Map of the Database IDs (getId():Long) and associated severity for this group 
		 */
		Map<Integer,String> tempMap = new HashMap<>();
		Iterator<String> iter = this.optseverity.iterator();
		while (iter.hasNext()) {
			String[] exploded = iter.next().split("-");
			Integer l = new Integer(exploded[0]);
			tempMap.put(l, exploded[1]);
		}
		return tempMap;
	}
	
	public void addReference(Component component) {
		super.addReference(component);
	}
	
	public void addReference(Component component, String severity){ //If the Component comes with a severity
		if(optseverity == null) optseverity = new HashSet<String>();
		
		//If Component is not a Constraint or Concept, but s.o. wants to add a severity though, it is not possible
		if((component.getType()==ComponentType.CONSTRAINT) || (component.getType()==ComponentType.CONCEPT)){ 
			super.addReference(component); 
			optseverity.add(component.getId().toString() + "-" + severity);
		} else {
			throw new IllegalArgumentException("Cannot add " + component.getType() + " to " + this.getType());
		}
		
	}
	
	public void deleteReference(Component component){
		//removing the additional Severity just possible, if the component is a Concept or a Constraint
		if(optseverity != null && ( (component.getType()==ComponentType.CONSTRAINT) || (component.getType()==ComponentType.CONCEPT))){
			Iterator<String> iter = optseverity.iterator();
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
