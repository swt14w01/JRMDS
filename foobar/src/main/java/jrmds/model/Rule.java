package jrmds.model;

public abstract class Rule extends SubComponent {
	private String severity;

	public Rule() {
		// blubb
	}

	public Rule(String refID, ComponentType type) {
		super(refID, type);
		//default severity
		this.severity = "info";
	}

	public Rule(Component component) {
		super(component);
		severity = component.getSeverity();
	}

	@Override
	public String getSeverity() {
		return severity;
	}

	@Override
	public void setSeverity(String sev) {
		//check if severity is in List
		switch (sev) {
		case "blocker": break;
		case "critical": break;
		case "major": break;
		case "minor": break;
		case "info": break;
		default: throw new IllegalArgumentException("Severity is wrong and not in list");
		}
		this.severity = sev;
	}
}
