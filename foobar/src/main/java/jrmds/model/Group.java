package jrmds.model;

import java.util.HashMap;
import java.util.Map;


public class Group extends Component {
	private Map<Component, String> optseverity;
	
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
	
	public Map<Component, String> getOptSeverity(){
		return this.optseverity;
	}
	
	public boolean addReference(Component component, String severity){ //If the Component comes with a severity
		if(optseverity == null) optseverity = new HashMap<Component, String>();
		
		//If Component is not a Constraint or Concept, but s.o. wants to add a severity though, it is not possible
		if((component.getType()==ComponentType.CONSTRAINT)||(component.getType()==ComponentType.CONCEPT)){ 
			boolean success = super.addReference(component); //checking if allowed dependencies are considered, if not: false
			if(success) { 
				optseverity.put(component, severity);
				return true;
			}
		}
		return false; 
	}
	
	public void deleteReference(Component component){
		//removing the additional Severity just possible, if the component is a Concept or a Constraint
		if((component.getType()==ComponentType.CONSTRAINT)||(component.getType()==ComponentType.CONCEPT)){
			optseverity.remove(component); 
		}
		super.deleteReference(component);
	}
}
