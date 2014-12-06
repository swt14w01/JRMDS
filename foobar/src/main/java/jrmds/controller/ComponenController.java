package jrmds.controller;

import jrmds.main.JrmdsManagement;
import jrmds.model.*;
import jrmds.user.UserManagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComponenController {
	@Autowired
	private JrmdsManagement controller;
	@Autowired
	private UserManagement usr;
	
	@RequestMapping(value="/editRule")
	public String htmlRules() {
		
		return "editRule";
	}
	
	@RequestMapping(value="/createGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String createGroup(
			Model model,
			@RequestParam(required=true, defaultValue="testpro") String project
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
			@RequestParam(required=true, defaultValue="testpro") String project,
			@RequestParam(required=true, defaultValue="fastcheck") String group
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		Group g = controller.getGroup(p, group);
		if (g==null) throw new IllegalArgumentException("Group " + group + " does not exist in project " + project);
		Set<Component> downstream = g.getReferencedComponents();
		Set<Component> upstream = controller.getReferencingComponents(p, g);
		Set<Component> orphaned = controller.getSingleReferencedNodes(p, g);
		
		String taglist = "";
		Iterator<String> iter = g.getTags().iterator();
		while (iter.hasNext()) {
			taglist += iter.next() + ";";
		}
		
		model.addAttribute("project", p);
		model.addAttribute("group", g);
		model.addAttribute("taglist", taglist);
		model.addAttribute("downstram", downstream);
		model.addAttribute("upstream", upstream);
		model.addAttribute("orphaned", orphaned);
		
		
		return "editGroup";
	}
	
	@RequestMapping(value="/saveGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String saveGroup(
			Model model,
			@RequestParam String project,
			@RequestParam String gOldID,
			@RequestParam String gRefID,
			@RequestParam String gTaglist
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
		
		model.addAttribute("project", p);
		model.addAttribute("group", g);
		model.addAttribute("message", "The group " + msg);
		
		//we come so far, so no exception was thrown
		return "confirmation";
	}
	
	@RequestMapping(value="/confirmDeleteGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String confirmDeleteGroup(
			Model model,
			@RequestParam String project,
			@RequestParam String gRefID
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
		
		model.addAttribute("project", p);
		model.addAttribute("group", g);
		model.addAttribute("message", "The group " + msg);
		
		//we come so far, so no exception was thrown
		return "confirmationDelete";
	}
	
	
	@RequestMapping(value="/editTemplate")
	public String htmlTemplate() {
		
		
		return "";
	}
}
