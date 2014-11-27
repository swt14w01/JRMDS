package jrmds.model;

public class Group extends Component {
	public Group() {
		//empty
	}
	public Group (String refID) {
		super(refID, ComponentType.GROUP);
	}
	public Group (Component component) {
		super(component.getRefID(), ComponentType.GROUP);
		super.setTags(component.getTags());
	}
}
