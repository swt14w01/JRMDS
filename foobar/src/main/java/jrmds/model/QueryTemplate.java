package jrmds.model;

public class QueryTemplate extends SubComponent {
	public QueryTemplate() {
		//empty for Hibernate
	}
	public QueryTemplate(String refID) {
		super(refID, ComponentType.TEMPLATE);
	}
	public QueryTemplate(Component component) {
		super(component);
	}
}
