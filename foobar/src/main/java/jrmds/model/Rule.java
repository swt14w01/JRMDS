package jrmds.model;

public abstract class Rule extends SubComponent {
	private String severity;

	public Rule() {
		// blubb
	}

	public Rule(String refID, ComponentType type) {
		super(refID, type);
	}

	public Rule(Component component) {
		super(component.getRefID(), component.getType());
		severity = component.getSeverity();
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String sev) {
		this.severity = sev;
	}
}
