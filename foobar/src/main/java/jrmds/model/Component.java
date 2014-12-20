package jrmds.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
		if(this.id==null) throw new NullPointerException("The id of this Component is null!");
		return id;
	}
	
	public void setId(Long id){
		if(id == null) throw new NullPointerException("The id you want to set for this Component is null!");
		this.id = id;
	}

	public String getRefID() {
		if(this.refID==null) throw new NullPointerException("The refID of this Component is null!");
		return refID;
	}

	public void setRefID(String refID) {
		if(refID == null) throw new NullPointerException("The refID you want to set for this Component is null!");
		this.refID = refID;
	}

	public ComponentType getType() {
		if(this.type==null) throw new NullPointerException("The type of this Component is null!");
		return type;
	}

	public void setType(ComponentType type) {
		if(type == null) throw new NullPointerException("The type you want to set for this Component is null!");
		this.type = type;
	}

	public List<String> getTags() {
		if (Tags == null) return new ArrayList<String>();
		return Tags;
	}

	public void addTag(String Tag) {
		if(Tag == null) throw new NullPointerException("The Tag you want to add to this Component is null!");
		this.Tags.add(Tag);
	}

	public void setTags(List<String> Tags) {
		if(Tags == null) throw new NullPointerException("The Tags you want to set for this Component are null!");
		this.Tags = Tags;
	}

	public void deleteTag(String Tag) {
		if(Tag == null) throw new NullPointerException("The Tag you want to delete in this Component is null!");
		this.Tags.remove(Tag);
	}

	public void addReference(Component cmpt) {
		if(cmpt == null) throw new NullPointerException("The Reference you want to add to this Component is null!");
		
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
		if (dependsOn == null) return new HashSet<Component>();
		return dependsOn;
	}

	public void deleteReference(Component cmpt) {
		if(cmpt == null) throw new NullPointerException("The Reference you want to delete in this Component is null!");
		Set<Component> tempSet = new HashSet<>(dependsOn);
		Iterator<Component> iter = tempSet.iterator();
		while (iter.hasNext()) {
			Component temp = iter.next();
			if (cmpt.getRefID().equals(temp.getRefID())) this.dependsOn.remove(temp);
		}
	}

	public void copy(Component cmpt) {
		if(cmpt == null) throw new NullPointerException("The Component you want to copy is null!");
		if(this.type!=cmpt.getType()) throw new IllegalArgumentException("You can not copy a different Type!");
		this.refID = cmpt.getRefID();
		this.type = cmpt.getType();
		this.Tags = cmpt.getTags();
		this.setCypher(cmpt.getCypher());
		this.setDescription(cmpt.getDescription());
		this.setParameters(cmpt.getParameters());
		this.setSeverity(cmpt.getSeverity());
	}

	// diese Methoden sind Platzhalter, damit Component Objekte zu den
	// jeweiligen Objekten konvertiert werden können
	public String getDescription() {
		return "NA";
	}

	public String getSeverity() {
		return "NA";
	}
	
	public Set<String> getGroupSeverity() {
		return new HashSet<>();
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
	
	public void deleteParameter(Parameter para) {
	}

	public void setSeverity(String sev) {
	}
}
