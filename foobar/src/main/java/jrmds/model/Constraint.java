package jrmds.model;

public class Constraint extends Rule {
	
	public Constraint() {
		
	}
	public Constraint(String refID) {
		super(refID, ComponentType.CONSTRAINT);
	}
	public Constraint(Component component) {
		super(component);
	}
}
