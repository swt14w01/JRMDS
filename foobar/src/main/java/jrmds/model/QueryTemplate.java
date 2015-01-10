package jrmds.model;

/**
 * This class represents all QueryTemplate objects.
 */
public class QueryTemplate extends SubComponent {
	
	/** Empty for Hibernate */
	public QueryTemplate() {
	}
	
	/**
	 * Constructor to create a new QueryTemplate object with a refID. Gives the refID and the type to the constructor in SubComponent.
	 * @param refID  The given refID for the new QueryTemplate object.
	 */
	public QueryTemplate(String refID) {
		super(refID, ComponentType.TEMPLATE);
	}
	
	/**
	 * Constructor to create a new QueryTemplate object with component. Gives the component to the constructor in SubComponent.
	 * @param component  The given component for the new QueryTemplate object.
	 */
	public QueryTemplate(Component component) {
		super(component);
	}
}
