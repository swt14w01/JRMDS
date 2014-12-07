package jrmds.model;

import javax.validation.constraints.NotNull;

public class SearchRequest {
	private boolean includeGroups;
	private boolean includeConcepts;
	private boolean includeConstraints;
	private boolean includeQueryTemplates;
	@NotNull
	private String searchTerm;

	private boolean checked;
	
	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean getIncludeGroups() {
		return includeGroups;
	}

	public void setIncludeGroups(boolean includeGroups) {
		this.includeGroups = includeGroups;
	}

	public boolean getIncludeConcepts() {
		return includeConcepts;
	}

	public void setIncludeConcepts(boolean includeConcepts) {
		this.includeConcepts = includeConcepts;
	}

	public boolean getIncludeConstraints() {
		return includeConstraints;
	}

	public void setIncludeConstraints(boolean includeConstraints) {
		this.includeConstraints = includeConstraints;
	}

	public boolean getIncludeQueryTemplates() {
		return includeQueryTemplates;
	}

	public void setIncludeQueryTemplates(boolean includeQueryTemplates) {
		this.includeQueryTemplates = includeQueryTemplates;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public SearchRequest() {
	}

}
