package jrmds.controller;

import jrmds.main.JrmdsManagement;
import jrmds.model.*;
import jrmds.user.UserManagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class ComponenController extends WebMvcConfigurerAdapter{
	@Autowired
	private JrmdsManagement controller;
	@Autowired
	private UserManagement usr;
	
	@RequestMapping(value="/editRule")
	public String editRule() {
		
		return "editRule";
	}
	
	@RequestMapping(value="/createGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String createGroup(
			Model model,
			@RequestParam(required=true, defaultValue="testpro") String project,
			SearchRequest searchRequest
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
				
		model.addAttribute("project", p);
		model.addAttribute("group", new Group(""));
		model.addAttribute("taglist", "");
		model.addAttribute("downstram", new HashSet<Component>());
		model.addAttribute("upstream", new HashSet<Component>());
		model.addAttribute("orphaned", new HashSet<Component>());
		
		
		return "editGroup";
	}
	
	@RequestMapping(value="/editGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String editGroup(
			Model model,
			@RequestParam(required=true) String project,
			@RequestParam(required=true) String group,
			@RequestParam(required=false, defaultValue="") String component,
			@RequestParam(required=false, defaultValue="") String type,
			SearchRequest searchRequest
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		Group g = controller.getGroup(p, group);
		if (g==null) throw new IllegalArgumentException("Group " + group + " does not exist in project " + project);
		Set<Component> downstream = g.getReferencedComponents();
		Set<Component> upstream = controller.getReferencingComponents(p, g);
		Set<Component> orphaned = controller.getSingleReferencedNodes(p, g);
		
		//if a reference is clicked to delete, delete it... if not present then ignore it
		Component c;
		switch (type) {
		case "GROUP": c = new Group(component); break;
		case "CONCEPT": c = new Concept(component); break;
		case "CONSTRAINT": c = new Constraint(component); break;
		case "TEMPLATE": c = new QueryTemplate(component); break;
		default: c = null;
		}
		c = controller.getComponent(p,c);
		if (c != null) {
			g.deleteReference(c);
			controller.saveComponent(p, g);
		}
		
		
		
		String taglist = "";
		if (g.getTags() != null) {
			Iterator<String> iter = g.getTags().iterator();
			while (iter.hasNext()) {
				taglist += iter.next() + ";";
			}
		}
		
		//overwrite the severity in downstream-set, because we do not need this for the list, but the optional severity
		Map<Integer,String> optseverity = g.getOptSeverity();
		Set<Component> tempSet = new HashSet<>(downstream); //we need a copy of the set, to iterate and change items at the same time
		Iterator<Component> iter = tempSet.iterator();
		while (iter.hasNext()) {
			Component temp = iter.next();
			if (optseverity.containsKey(temp.getId().intValue())) {
				//update the component inside the set
				downstream.remove(temp);
				temp.setSeverity(optseverity.get(temp.getId().intValue()));
				downstream.add(temp);
			}
		}
		
		model.addAttribute("project", p);
		model.addAttribute("group", g);
		model.addAttribute("taglist", taglist);
		model.addAttribute("downstram", downstream);
		model.addAttribute("upstream", upstream);
		model.addAttribute("orphaned", orphaned);
		model.addAttribute("searchRequest", searchRequest);
		
		return "editGroup";
	}
	
	@RequestMapping(value="/saveGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String saveGroup(
			Model model,
			@RequestParam String project,
			@RequestParam String gOldID,
			@RequestParam String gRefID,
			@RequestParam String gTaglist,
			SearchRequest searchRequest
			) {
		
		String msg = " was successfully updated.";
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (gRefID.length()<3) throw new IllegalArgumentException("ReferenceID to short");
		Group g = controller.getGroup(p, gOldID);
		if (g==null) {
			//Group isnt existing, create a new one
			Group temp = controller.getGroup(p, gRefID);
			if (temp != null) throw new IllegalArgumentException("Group with this ID already exist!");
			g = new Group(gRefID);
			msg = " was successfully created";
		} else {
			//check if old and new refID are identical, if not check if there is another Component with same ID
			if (!gRefID.equals(gOldID)) {
				Group temp = controller.getGroup(p, gRefID);
				if (temp != null) throw new IllegalArgumentException("Group with this ID already exist!");
				g.setRefID(gRefID);
			}
		}
		
		String[] tags = gTaglist.split(";");
		Set<String> tagSet = new HashSet<>(); //use a temporary set to exclude doubles
		for (int i = 0; i < tags.length; i++) {
			//no tags shorter then 1 char, and no spaces. 
			String temp = tags[i].replace(" ", "");
			if (temp.length() > 1) tagSet.add(temp);
		}
		List<String> tagList = new ArrayList<String>(tagSet);
		g.setTags(tagList);
		
		controller.saveComponent(p, g);
		
		model.addAttribute("message", "The group " + msg);
		model.addAttribute("linkRef","/editGroup?project="+project+"&group="+gRefID);
		model.addAttribute("linkPro","/projectOverview?project="+project);
		model.addAttribute("searchRequest", searchRequest);
		//we come so far, so no exception was thrown
		return "confirmation";
	}
	
	@RequestMapping(value="/referenceGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String referenceGroup(
			Model model,
			@RequestParam String project,
			@RequestParam String gRefID,
			@RequestParam String newRefID,
			@RequestParam String newType,
			@RequestParam String newSeverity,
			SearchRequest searchRequest
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		Group g = controller.getGroup(p, gRefID);
		if (g==null) throw new IllegalArgumentException("Group not found!");
		
		Component c;
		switch (newType) {
		case "GROUP": c=new Group(newRefID); break;
		case "CONCEPT": c=new Concept(newRefID); break;
		case "CONSTRAINT": c=new Constraint(newRefID); break;
		default: throw new IllegalArgumentException("Component-type not specified");
		}
		
		String msg = "The component "+newRefID+" is now part of the Group "+gRefID+".";

		//check if reference ID is existent
		c = controller.getComponent(p, c);
		if (c == null) throw new NullPointerException("Component with ID "+newRefID+" didnt exist in project "+project);
		
		switch (newType) {
		case "GROUP": controller.addComponentRef(p, g, c); break;
		default: controller.addGroupRef(p, g, c, newSeverity);
		}
		controller.saveComponent(p, g);
		
		
		model.addAttribute("message",msg);
		model.addAttribute("linkRef","/editGroup?project="+project+"&group="+gRefID);
		model.addAttribute("linkPro","/projectOverview?project="+project);
		model.addAttribute("searchRequest", searchRequest);
		return "confirmation";
	}
	
	@RequestMapping(value="/udpateSeverity", method={RequestMethod.POST, RequestMethod.GET})
	public String updateSeverity(
			Model model,
			@RequestParam String project,
			@RequestParam String gRefID,
			@RequestParam(value = "toUpdateSev") String[] toUpdateSev,
			@RequestParam(value = "toUpdateRefID") String[] toUpdateRefID,
			@RequestParam(value = "toUpdateType") String[] toUpdateType,
			SearchRequest searchRequest
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		Group g = controller.getGroup(p, gRefID);
		if (g==null) throw new IllegalArgumentException("Group not found!");
	
		String msg = "Updated Severity";
		
		for (int i = 0; i < toUpdateSev.length; i++) {
			//retrieve every component of the returned list and because the order may vary, we need to check for every component manually
			//delete the reference and update it again
			Component c;
			switch (toUpdateType[i]) {
			case "GROUP": c=new Group(toUpdateRefID[i]); break;
			case "CONCEPT": c=new Concept(toUpdateRefID[i]); break;
			case "CONSTRAINT": c=new Constraint(toUpdateRefID[i]); break;
			default: throw new IllegalArgumentException("Component-type not specified");
			}
			c = controller.getComponent(p,c);
			if (c != null) {
				g.deleteReference(c);
				g.addReference(c, toUpdateSev[i]);
			}
		}
		controller.saveComponent(p, g);

	
		
		model.addAttribute("message",msg);
		model.addAttribute("linkRef","/editGroup?project="+project+"&group="+gRefID);
		model.addAttribute("linkPro","/projectOverview?project="+project);
		model.addAttribute("searchRequest", searchRequest);
		return "confirmation";
	}
	
	@RequestMapping(value="/confirmDeleteGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String confirmDeleteGroup(
			Model model,
			@RequestParam String project,
			@RequestParam String gRefID,
			SearchRequest searchRequest
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		Group g = controller.getGroup(p, gRefID);
		if (g==null) throw new IllegalArgumentException("Group not found!");
		
		//get all Nodes, which would be orphaned
		Set<Component> orphaned = controller.getSingleReferencedNodes(p, g);
		
		model.addAttribute("project", p);
		model.addAttribute("group", g);
		model.addAttribute("orphaned",orphaned);
		model.addAttribute("searchRequest", searchRequest);
		
		return "confirmationDelete";
	}
	@RequestMapping(value="/DeleteGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String DeleteGroup(
			Model model,
			@RequestParam String project,
			@RequestParam String gRefID,
			SearchRequest searchRequest
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		Group g = controller.getGroup(p, gRefID);
		if (g==null) throw new IllegalArgumentException("Group not found!");
		
		controller.deleteComponent(p, g);
		
		String msg = "The group "+g.getRefID()+" was successfully deleted!";

		model.addAttribute("message",msg);
		model.addAttribute("linkRef","");
		model.addAttribute("linkPro","/projectOverview?project="+project);
		model.addAttribute("searchRequest", searchRequest);
		
		return "confirmation";
	}
	
	
	@RequestMapping(value="/editTemplate")
	public String editTemplate() {
		
		
		return "";
	}
}
