package jrmds.model;

public class ImportReferenceError {

	String _itemId;
	String _referenceId;
	ComponentType _expectedType;
	ComponentType _referencedType;

	
	public ImportReferenceError(String itemId, String referenceId,	ComponentType expectedType)
	{
		this(itemId, referenceId, expectedType, null);
	}
	
	public ImportReferenceError(String itemId, String referenceId,	ComponentType expectedType, ComponentType referencedType)
	{
		_itemId = itemId;
		_referenceId = referenceId;
		_expectedType = expectedType;
		_referencedType = referencedType;
	}
	
	
	public String getItemId()
	{
		return _itemId;
	}
	
	public String getReferenceId()
	{
		return _referenceId;
	}
	
	public ComponentType getExpectedType()
	{
		return _expectedType;
	}
	/**
	 * If ReferencedType is null, there is no component at the end of the reference. If its not null, there is an other Type then expected
	 * @return
	 */
	public ComponentType getReferencedType()
	{
		return _referencedType;
	}
	
}
