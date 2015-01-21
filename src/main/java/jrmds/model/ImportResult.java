package jrmds.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the import of all components from a xml document.
 */
public class ImportResult {
	/** The list of components which can be imported */
	List<ImportItem> _itemList = new ArrayList<ImportItem>();
	/** The list of components which habe reference errors. */
	List<ImportReferenceError> _refErrList = new ArrayList<ImportReferenceError>();
	
	
	/**
	 * Adds the given item to the itemList.
	 * @param item	given item
	 */
	public void AddImportItem(ImportItem item)
	{
		_itemList.add(item);
	}
	
	/**
	 * Adds the given item to the referenceErrorList.
	 * @param item	given item
	 */
	public void AddImportReferenceError(ImportReferenceError item)
	{
		_refErrList.add(item);
	}

	/**
	 * Gets an Iterable over the itemList.
	 * @return _itemList
	 */
	public Iterable<ImportItem> iterateImportItems()
	{
		return _itemList;
	}

	/**
	 * Gets an Iterable over the refErrList.
	 * @return _refErrList
	 */
	public Iterable<ImportReferenceError> iterateImportReferenceError()
	{
		return _refErrList;
	}
	
	/**
	 * Gets the itemList.
	 * @return _itemList
	 */
	public int getImportItemSize(){
		return this._itemList.size();
	}
	
	/**
	 * Gets the refErrList.
	 * @return _refErrList
	 */
	public int getImportReferenceErrorSize(){
		return this._refErrList.size();
	}
	
}
