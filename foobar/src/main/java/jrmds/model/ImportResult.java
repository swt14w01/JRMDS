package jrmds.model;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
	
	List<ImportItem> _itemList = new ArrayList<ImportItem>();
	
	
	
	public void AddImportItem(ImportItem item)
	{
		_itemList.add(item);
	}
	
	public Iterable<ImportItem> iterateImportItems()
	{
		return _itemList;
	}
	
}
