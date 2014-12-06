package jrmds.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public abstract class Component {
	@GraphId
	private Long id;
	private String refID;
	private List<String> Tags;
	private ComponentType type;
	@RelatedTo(type = "DEPENDSON", direction = Direction.OUTGOING)
	private @Fetch Set<Component> dependsOn;

	public Component() {
	}

	public Component(String refID, ComponentType type) {
		this.refID = refID;
		this.type = type;
		this.Tags = new ArrayList<String>();
		this.dependsOn = new HashSet<Component>();
	}
	
	public Component(Component component) {
		this.refID = component.getRefID();
		this.type = component.getType();
		this.Tags = component.getTags();
		this.dependsOn = component.getReferencedComponents();
		this.id = component.getId();
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}

	public String getRefID() {
		return refID;
	}

	public void setRefID(String refID) {
		this.refID = refID;
	}

	public ComponentType getType() {
		return type;
	}

	public void setType(ComponentType type) {
		this.type = type;
	}

	public List<String> getTags() {
		return Tags;
	}

	public void addTag(String Tag) {
		this.Tags.add(Tag);
	}

	public void setTags(List<String> Tags) {
		this.Tags = Tags;
	}

	public void deleteTag(String Tag) {
		this.Tags.remove(Tag);
	}

	public void addReference(Component cmpt) {
		if (this.dependsOn == null)	dependsOn = new HashSet<Component>();
		if (this.dependsOn.contains(cmpt)) throw new DuplicateKeyException("Component " + cmpt.getRefID() + " is already referenced");
		
		// check illegal dependencies
		if (this.type == ComponentType.TEMPLATE) throw new IllegalArgumentException("Cannot add TEMPLATE to " + this.getType());
		if (this.type == ComponentType.GROUP) {
			if (cmpt.getType() == ComponentType.TEMPLATE) throw new IllegalArgumentException("Cannot add TEMPLATE to " + this.getType());
		}
		
		if (this.type == ComponentType.CONCEPT || this.type == ComponentType.CONSTRAINT) {
			if ((cmpt.getType() == ComponentType.GROUP) || (cmpt.getType()==ComponentType.CONSTRAINT)) throw new IllegalArgumentException("Cannot add " + cmpt.getType() + " to " + this.getType());
			if(cmpt.getType()==ComponentType.TEMPLATE) {
				for(Component template:dependsOn) {
					if (template.getType() == ComponentType.TEMPLATE) throw new IllegalArgumentException("Cannot add " + cmpt.getType() + " to " + this.getType());;
				}
			}
		}
		dependsOn.add(cmpt);
	}
	
	public Set<Component> getReferencedComponents() {
		return dependsOn;
	}

	public void deleteReference(Component cmpt) {
		this.dependsOn.remove(cmpt);
	}

	public void copy(Component cmpt) {
		this.refID = cmpt.getRefID();
		this.type = cmpt.getType();
		this.Tags = cmpt.getTags();
		this.setCypher(cmpt.getCypher());
		this.setDescription(cmpt.getDescription());
		this.setSeverity(cmpt.getSeverity());
		this.setParameters(cmpt.getParameters());
	}

	// diese Methoden sind Platzhalter, damit Component Objekte zu den
	// jeweiligen Objekten konvertiert werden können
	public String getDescription() {
		return "NA";
	}

	public String getSeverity() {
		return "NA";
	}

	public String getCypher() {
		return "NA";
	}

	public Set<Parameter> getParameters() {
		return new HashSet<Parameter>();
	}

	public void setDescription(String desc) {
	}

	public void setCypher(String cypher) {
	}

	public void setParameters(Set<Parameter> para) {
	}

	public void addParameter(Parameter para) {
	}

	public void setSeverity(String sev) {
	}
}
