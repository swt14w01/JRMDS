package jrmds.model;


public class ImportItem {

	
	EnumConflictCause _cause;
	Component _component;
	ComponentType _inConflictWith;
	String _additionalInfo;


	public ImportItem(Component component)
	{
		this(component, EnumConflictCause.None, null);
	}

	public ImportItem(Component component, EnumConflictCause cause, ComponentType inConflictWith)
	{
		this(component, cause, inConflictWith, null);
	}

	public ImportItem(Component component, EnumConflictCause cause, ComponentType inConflictWith, String additionalInfo)
	{
		_component = component;
		_cause = cause;
		_inConflictWith = inConflictWith;
		_additionalInfo = additionalInfo;
	}
	
	
	public EnumConflictCause getCause()
	{
		return _cause;
	}

	public Component getComponent()
	{
		return _component;
	}

	public ComponentType getInConflictWith()
	{
		return _inConflictWith;
	}
	
	public String getAdditionalInfo()
	{
		return _additionalInfo;
	}

}
