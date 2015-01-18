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
	
	public ComponentType getReferencedType()
	{
		return _referencedType;
	}
	
}
