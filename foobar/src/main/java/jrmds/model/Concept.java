package jrmds.model;
/**
 *This class represents all Concept objects.
 */
public class Concept extends Rule {
	
	String _useQueryTemplate; 
	
	
	/** Empty for Hibernate */
	public Concept() {
	}
	
	/**
	 * Constructor to create a new Concept object with a refID. Gives the refID and the type to the constructor in Rule.
	 * @param refID  The given refID for the new Concept object.
	 */
	public Concept(String refID) {
		super(refID, ComponentType.CONCEPT);
	}
	
	/**
	 * Constructor to create a new Concept object with component. Gives the component to the constructor in Rule.
	 * @param component  The given component for the new Concept object.
	 */
	public Concept(Component component) {
		super(component);
	}
	
	/**
	 * UseQueryTemplate
	 * @return Id
	 */
	public String getUseQueryTemplate()
	{
		return _useQueryTemplate;
	}
	
	/**
	 * set the UseQueryTemplate
	 * @param value
	 */
	public void setUseQueryTemplate(String value)
	{
		_useQueryTemplate = value;
	}
	
}
