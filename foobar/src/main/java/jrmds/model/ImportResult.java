package jrmds.model;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
	
	List<ImportItem> _itemList = new ArrayList<ImportItem>();
	List<ImportReferenceError> _refErrList = new ArrayList<ImportReferenceError>();
	
	
	
	public void AddImportItem(ImportItem item)
	{
		_itemList.add(item);
	}
	
	public void AddImportReferenceError(ImportReferenceError item)
	{
		_refErrList.add(item);
	}


	public Iterable<ImportItem> iterateImportItems()
	{
		return _itemList;
	}

	public Iterable<ImportReferenceError> iterateImportReferenceError()
	{
		return _refErrList;
	}
	
	public List<ImportItem> getImportItem(){
		return this._itemList;
	}
	
	public List<ImportReferenceError> getImportReferenceError(){
		return this._refErrList;
	}
	
}
