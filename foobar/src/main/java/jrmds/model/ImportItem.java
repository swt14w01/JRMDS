package jrmds.model;

/**
 * Class that represents a xml import of one component into the project.
 */
public class ImportItem {

	/** Cause for a conflict. If no conflict appeared: is none */
	EnumConflictCause _cause;
	/** Component that is to be imported */
	Component _component;
	/** Component which has a conclict with the _component */
	ComponentType _inConflictWith;
	/** Additional information to the components and conflicts*/
	String _additionalInfo;


	/**
	 * Constructor to create an ImportItem with a given component without conflict.
	 * @param component	given component
	 */
	public ImportItem(Component component)
	{
		this(component, EnumConflictCause.None, null);
	}

	/**
	 * Constructor to create an ImportItem with a given component with conflict.
	 * @param component	given component
	 * @param cause 	given cause
	 * @param inConflictWith	given conflict component
	 */
	public ImportItem(Component component, EnumConflictCause cause, ComponentType inConflictWith)
	{
		this(component, cause, inConflictWith, null);
	}

	/**
	 * Constructor to create an ImportItem with a given component with conflict.
	 * @param component	given component
	 * @param cause 	given cause
	 * @param inConflictWith	given conflict component
	 * @param additionalInfo	given additional information to the conflict
	 */
	public ImportItem(Component component, EnumConflictCause cause, ComponentType inConflictWith, String additionalInfo)
	{
		_component = component;
		_cause = cause;
		_inConflictWith = inConflictWith;
		_additionalInfo = additionalInfo;
	}
	
	/**
	 * Gets the cause.
	 * @return _cause
	 */
	public EnumConflictCause getCause()
	{
		return _cause;
	}

	/**
	 * Gets the component.
	 * @return _component
	 */
	public Component getComponent()
	{
		return _component;
	}

	/**
	 * Gets the conflict component.
	 * @return _inConflictWith
	 */
	public ComponentType getInConflictWith()
	{
		return _inConflictWith;
	}
	
	/**
	 * Gets the additional information
	 * @return _additionalInfo
	 */
	public String getAdditionalInfo()
	{
		return _additionalInfo;
	}

}
