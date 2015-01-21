package jrmds.model;

/**
 * This class represents all Constraint objects.
 */
public class Constraint extends Rule {
	
	/** Empty for Hibernate */
	public Constraint() {
	}
	
	/**
	 * Constructor to create a new Constraint object with a refID. Gives the refID and the type to the constructor in Rule.
	 * @param refID  The given refID for the new Constraint object.
	 */
	public Constraint(String refID) {
		super(refID, ComponentType.CONSTRAINT);
	}
	
	/**
	 * Constructor to create a new Constraint object with component. Gives the component to the constructor in Rule.
	 * @param component  The given component for the new Constraint object.
	 */
	public Constraint(Component component) {
		super(component);
	}
}
