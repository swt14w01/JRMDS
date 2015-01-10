package jrmds.model;

/**
 * This class is a container for concepts and constraints.
 */
public abstract class Rule extends SubComponent {
	/** Sets the priority for this Rule for the JQAssistant.*/
	private String severity;

	public Rule() {
	}

	/**
	 * Constructor to create a new Rule with a refID and a Component type. 
	 * Gives the refID and the type of the Component to the constuctor in SubComonent.
	 * @param refID
	 * @param type
	 */
	public Rule(String refID, ComponentType type) {
		super(refID, type);
		/**
		 * The default severity for constraints is major, for concepts it is info.
		 */
		if(type==ComponentType.CONSTRAINT) this.severity = "major";
		else this.severity = "info";
	}
	
	/**
	 * Constructor to create a new Rule with a given component.
	 * Gives the component to the constuctor in SubComonent.
	 * @param component  The component which is to be used to create the new rule.
	 */
	public Rule(Component component) {
		super(component);
		severity = component.getSeverity();
	}

	/**
	 * Gets the current severity of the Rule.
	 * @return severity
	 */
	@Override
	public String getSeverity() {
		if (severity==null) return "";
		return severity;
	}

	/**
	 * Sets the severity of the current Rule.
	 * @param sev  The severity which is used to overwrite the current severity.
	 * @throws NullPointerException if the String sev is null.
	 * @throws IllegalArgumentException if the String sev doesn't match any of the acceptable severitiy options.
	 */
	@Override
	public void setSeverity(String sev) {
		if(sev == null) throw new NullPointerException("The severity you want to set for this Component is null!");
		//check if severity is in List
		switch (sev) {
		case "blocker": break;
		case "critical": break;
		case "major": break;
		case "minor": break;
		case "info": break;
		default: throw new IllegalArgumentException("Severity is wrong and not in list");
		}
		this.severity = sev;
	}
}
