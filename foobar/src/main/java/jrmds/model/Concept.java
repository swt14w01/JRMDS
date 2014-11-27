package jrmds.model;

public class Concept extends Rule {
	public Concept() {
		//empty Constructor for Hibernate
	}
	public Concept(String refID) {
		super(refID, ComponentType.CONCEPT);
	}
	public Concept(Component component) {
		super(component);
	}
}
