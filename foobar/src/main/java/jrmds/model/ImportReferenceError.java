package jrmds.model;

/**
 * Class that represents a xml import of one component with reference errors into the project.
 */
public class ImportReferenceError {

	/** Id of the component which is to be imported */
	String _itemId;
	/**Id of the reference component which is to be imported */
	String _referenceId;
	/**Type that is expected of the reference component */
	ComponentType _expectedType;
	/** Actual type of the reference component */
	ComponentType _referencedType;
	/**Type of the component which is to be imported */
	ComponentType _itemType;

	/**
	 * Constructor for a component with missing reference component 
	 * @param itemId	given component refID
	 * @param itemType	given component type
	 * @param referenceId	given reference component refID
	 * @param expectedType	given expected reference component type
	 */
	public ImportReferenceError(String itemId, ComponentType itemType, String referenceId,	ComponentType expectedType)
	{
		this(itemId, itemType, referenceId, expectedType, null);
	}
	
	/**
	 * Constructor for a component with reference component that has a unfitting type
	 * @param itemId	given component refID
	 * @param itemType	given component type
	 * @param referenceId	given reference component refID
	 * @param expectedType	given expected reference component type
	 * @param referencedType	given type of the reference component
	 */
	public ImportReferenceError(String itemId, ComponentType itemType, String referenceId,	ComponentType expectedType, ComponentType referencedType)
	{
		_itemId = itemId;
		_itemType = itemType;
		_referenceId = referenceId;
		_expectedType = expectedType;
		_referencedType = referencedType;
	}
	
	/**
	 * Gets the component refID
	 * @return _itemId
	 */
	public String getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Gets the component type
	 * @return _itenType
	 */
	public ComponentType getItemType()
	{
		return _itemType;
	}
	
	
	/**
	 * Gets the refID of the reference component.
	 * @return _referenceId
	 */
	public String getReferenceId()
	{
		return _referenceId;
	}
	
	/**
	 * Gets the expected type of the reference component.
	 * @return _expectedType
	 */
	public ComponentType getExpectedType()
	{
		return _expectedType;
	}
	/**
	 * Gets the actual reference component type
	 * If ReferencedType is null, there is no component at the end of the reference. If its not null, there is an other Type then expected
	 * @return _referencedType
	 */
	public ComponentType getReferencedType()
	{
		return _referencedType;
	}
	
}
