package jrmds.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jrmds.main.JrmdsManagement;
import jrmds.model.Component;
import jrmds.model.ComponentType;
import jrmds.model.Concept;
import jrmds.model.Constraint;
import jrmds.model.Group;
import jrmds.model.Parameter;
import jrmds.model.Project;
import jrmds.model.QueryTemplate;
import jrmds.model.RegistredUser;
import jrmds.security.CurrentUser;
import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.Template;

@Controller
public class ComponenController {
	/** An instance of the JrmdsManagement class to handle the database communication. */
	@Autowired
	private JrmdsManagement controller;
	/** An instance of the UserManahement class to handle users.*/
	@Autowired
	private UserManagement usr;

		
	
/*
 ********************************************************************************************************* 
 *							RULE
 ********************************************************************************************************* 
 */
	
	/**
	 * Request call to create new Rules.
	 * @param model
	 * @param regUser	The user status.
	 * @param project  Name of the current project.
	 * @param type
	 * @return editRule
	 */
	@RequestMapping(value="/createRule", method={RequestMethod.POST, RequestMethod.GET})
	public String createRule(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam(required=true) String project,
			@RequestParam(required=true) String type
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
				
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		
		model.addAttribute("project", p);
		switch (type) {
		case "CONCEPT": model.addAttribute("rule", new Concept("")); break;
		case "CONSTRAINT": model.addAttribute("rule", new Constraint("")); break;
		default: throw new IllegalArgumentException("Supplied type needs to be concept or constraint!");
		}

		model.addAttribute("taglist", "");
		model.addAttribute("downstram", new HashSet<Component>());
		model.addAttribute("upstream", new HashSet<Component>());
		model.addAttribute("parameters", new HashSet<Parameter>());
		model.addAttribute("orphaned", new HashSet<Component>());
		model.addAttribute("createRule", new Boolean(true));
		
		return "editRule";
	}
	
	@RequestMapping(value="/editRule", method={RequestMethod.POST, RequestMethod.GET})
	public String editRule(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam(required=true) String project,
			@RequestParam(required=true) String rule,
			@RequestParam(required=true) String type
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");

		Component r;
		switch (type) {
		case "CONCEPT":  r = controller.getConcept(p, rule); break;
		case "CONSTRAINT": r = controller.getConstraint(p, rule); break;
		default: throw new IllegalArgumentException("Supplied type needs to be concept or constraint!");
		}
		
		if (r == null) throw new IllegalArgumentException("Rule " + rule + " does not exist in project " + project);
		Set<Component> downstream = r.getReferencedComponents();
		Set<Component> upstream = controller.getReferencingComponents(p, r);
		Set<Component> orphaned = controller.getSingleReferencedNodes(p, r);
		Set<Parameter> parameters = r.getParameters();
		
				
		String taglist = "";
		if (r.getTags() != null) {
			Iterator<String> iter = r.getTags().iterator();
			while (iter.hasNext()) {
				taglist += iter.next() + ",";
			}
		}
		
		//check if a template is referenced
		String templateUsed = "false";
		Iterator<Component> iter = downstream.iterator();
		while (iter.hasNext()) {
			if (iter.next().getType() == ComponentType.TEMPLATE) templateUsed = "true";
		}
		
		
		model.addAttribute("project", p);
		model.addAttribute("rule", r);
		model.addAttribute("taglist", taglist);
		model.addAttribute("downstram", downstream);
		model.addAttribute("upstream", upstream);
		model.addAttribute("orphaned", orphaned);
		model.addAttribute("parameters", parameters);
		model.addAttribute("templateUsed", templateUsed);
		model.addAttribute("createRule", new Boolean(false));
		
		if (regUser == null || !usr.workingOn(regUser, p)) return "guesteditRule";
		else return "editRule";
	
	}
	
	@RequestMapping(value="/saveRule", method={RequestMethod.POST, RequestMethod.GET})
	public String saveRule(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String rOldID,
			@RequestParam String rRefID,
			@RequestParam String rDesc,
			@RequestParam String rCypher,
			@RequestParam String rSeverity,
			@RequestParam String rType,
			@RequestParam String rTaglist
			) {
		
		String msg = " was successfully updated.";
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		if (rRefID.length()<3) throw new IllegalArgumentException("ReferenceID to short");
	
		Component r;
		switch (rType) {
		case "CONCEPT":  r = controller.getConcept(p, rOldID); break;
		case "CONSTRAINT": r = controller.getConstraint(p, rOldID); break;
		default: throw new IllegalArgumentException("Supplied type needs to be concept or constraint!");
		}
		
		//check, if the new RefID is in use
		Component temp;
		switch (rType) {
		case "CONCEPT":  temp = controller.getConcept(p, rRefID); break;
		case "CONSTRAINT": temp = controller.getConstraint(p, rRefID); break;
		default: temp = null;
		}
		
		if (r == null) {
			//Rule isn't existing, create a new one
			if (temp != null) throw new IllegalArgumentException("Rule with this ID already exist!");
			switch (rType) {
			case "CONCEPT":  r = new Concept(rRefID); break;
			case "CONSTRAINT": r = new Constraint(rRefID); break;
			}
			msg = " was successfully created";
		} else {
			//check if old and new refID are identical, if not check if there is another Component with same ID
			if (!rRefID.equals(rOldID))	{
				if (temp != null) throw new IllegalArgumentException("Rule with this ID already exist!");
				r.setRefID(rRefID);
			}
		}
		
		String[] tags = rTaglist.split(",");
		Set<String> tagSet = new HashSet<>(); //use a temporary set to exclude doubles
		for (int i = 0; i < tags.length; i++) {
			//no tags shorter then 1 char, and no spaces. 
			String tempTag = tags[i].replace(" ", "");
			if (tempTag.length() > 1) tagSet.add(tempTag);
		}
		List<String> tagList = new ArrayList<String>(tagSet);
		r.setTags(tagList);
		r.setCypher(rCypher);
		r.setDescription(rDesc);
		r.setSeverity(rSeverity);
		controller.saveComponent(p, r);
		
		model.addAttribute("message", "The rule " + msg);
		model.addAttribute("linkRef","/editRule?project="+project+"&rule="+rRefID+"&type="+rType);
		model.addAttribute("linkPro","/projectOverview?project="+project);
		
		//we come so far, so no exception was thrown
		return "confirmation";
	}
	
	@RequestMapping(value="/referenceRule", method={RequestMethod.POST, RequestMethod.GET})
	public String referenceRule(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String rRefID,
			@RequestParam String rType,
			@RequestParam String newRefID,
			@RequestParam String newType
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		
		Component r;
		switch (rType) {
		case "CONCEPT":  r = controller.getConcept(p, rRefID); break;
		case "CONSTRAINT": r = controller.getConstraint(p, rRefID); break;
		default: throw new IllegalArgumentException("Supplied type needs to be concept or constraint!");
		}
		
		if (r == null) throw new IllegalArgumentException("Rule " + rRefID + " does not exist in project " + project);
		
		Component c;
		switch (newType) {
		case "TEMPLATE": c=new QueryTemplate(newRefID); break;
		case "CONCEPT": c=new Concept(newRefID); break;
		case "CONSTRAINT": c=new Constraint(newRefID); break;
		default: throw new IllegalArgumentException("Component-type not specified");
		}
		
		String msg = "The component "+newRefID+" is now part of the rule "+rRefID+".";

		//check if reference ID is existent
		c = controller.getComponent(p, c);
		if (c == null) throw new NullPointerException("Component with ID "+newRefID+" didnt exist in project "+project);
		
		controller.addComponentRef(p, r, c);
		controller.saveComponent(p, r);
		
		
		model.addAttribute("message",msg);
		model.addAttribute("linkRef","/editRule?project="+project+"&rule="+rRefID+"&type="+rType);
		model.addAttribute("linkPro","/projectOverview?project="+project);
		
		return "confirmation";
	}
	
	@RequestMapping(value="/testReferences", method={RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody Map<String, String> testing(
			@CurrentUser RegistredUser regUser,
			@RequestParam String projectName,
			@RequestParam String ruleName,
			@RequestParam String input,
			@RequestParam String ruleType
			) {
		
		Map<String, String> componentsAvailable = new HashMap<>();

		Project project = controller.getProject(projectName);
		if (project == null) throw new IllegalArgumentException("Project-name " + projectName + " invalid, Project not existent");
		//if (regUser == null || !usr.workingOn(regUser, project)) throw new IllegalArgumentException("you are not allowed to do this!");
		
		Set<Component> componentSet = new HashSet<>(project.getComponents());
		
		Component actualComponent = null;
		switch(ruleType) {
		case ("GROUP"):
			actualComponent = new Group(controller.getGroup(project, ruleName));
			break;
		case ("CONCEPT"):
			actualComponent = new Concept(controller.getConcept(project, ruleName));
			break;
		case ("CONSTRAINT"):
			actualComponent = new Constraint(controller.getConstraint(project, ruleName));
			break;
		case("TEMPLATE"):
			actualComponent = null;
		break;
		}		
		if (actualComponent == null || input.isEmpty()) return componentsAvailable;
		
		//if already a template is linked, we cannot add another one
		Boolean hasTemplate = false;
		for (Component ref : actualComponent.getReferencedComponents()) {
			if (ref.getType().equals(ComponentType.TEMPLATE)) hasTemplate = true;
		}
		
		//clear out all components, already references from the current rule
		componentSet = controller.getIntersection(componentSet, actualComponent.getReferencedComponents(), true);
		// do not allow cycles between components
		componentSet = controller.getIntersection(componentSet, controller.getReferencingComponents(project, actualComponent), true);
		
		input = input.toLowerCase();
		
		//search for components matching the input, dropping the remainder
		Set<Component> tempSet = new HashSet<>();
		for (Component potentialReferenceComponent : componentSet) {
			if (!potentialReferenceComponent.getRefID().equals(ruleName) && potentialReferenceComponent.getRefID().toLowerCase().contains(input)) tempSet.add(potentialReferenceComponent);
		}
		
		//select only the allowed candidates
		for (Component potentialReferenceComponent : tempSet) {			
			switch (ruleType) {
			case ("GROUP"):
				if (!potentialReferenceComponent.getType().equals(ComponentType.TEMPLATE)) componentsAvailable.put(potentialReferenceComponent.getRefID(), potentialReferenceComponent.getType().toString());  
				break;
			case ("CONCEPT"): //fall through to CONSTRAINT because similar
			case ("CONSTRAINT"):
				if (potentialReferenceComponent.getType().equals(ComponentType.TEMPLATE)) {
					if (!hasTemplate) {
						componentsAvailable.put(potentialReferenceComponent.getRefID(), potentialReferenceComponent.getType().toString());
						hasTemplate = true;
					}
				}
				if (potentialReferenceComponent.getType().equals(ComponentType.CONCEPT)) {
					componentsAvailable.put(potentialReferenceComponent.getRefID(), potentialReferenceComponent.getType().toString());
				}
				break;
			case("TEMPLATE"):
				
				break;
			}
		}	
			
		
		return componentsAvailable;
	}
	
	
	@RequestMapping(value="/udpateParameters", method={RequestMethod.POST, RequestMethod.GET})
	public String updateParameters(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String rRefID,
			@RequestParam String rType,
			@RequestParam(required=false, defaultValue="", value = "toUpdateId") String[] toUpdateId,
			@RequestParam(required=false, defaultValue="", value = "toUpdateName") String[] toUpdateName,
			@RequestParam(required=false, defaultValue="", value = "toUpdateValue") String[] toUpdateValue,
			@RequestParam(required=false, defaultValue="", value = "isString") String[] isString
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
			
		Component r;
		switch (rType) {
		case "CONCEPT":  r = controller.getConcept(p, rRefID); break;
		case "CONSTRAINT": r = controller.getConstraint(p, rRefID); break;
		case "TEMPLATE": r = controller.getTemplate(p, rRefID); break;
		default: throw new IllegalArgumentException("Supplied type needs to be concept or constraint!");
		}
		if (r == null) throw new IllegalArgumentException("Rule " + rRefID + " does not exist in project " + project);
	
		String msg = "Updated Parameters";
		
		controller.deleteAllParameters(p, r);
		
		if (toUpdateName.length>0) {
			for (int i = 0; i < toUpdateName.length; i++) {
				//iterate through the Arrays of parameteres. We need to remember, that a checkbox entry is only returned, when it is checked. Otherwise there is no returned value
				Boolean b = false;
				if (isString.length>0) for (int j=0; j < isString.length; j++) if (isString[j].equals(toUpdateId[i])) b=true;
				if (toUpdateName[i].length()>0 && toUpdateValue[i].length()>0) {
					Boolean paraContained = false;
					Iterator<Parameter> paraIter = r.getParameters().iterator();
					while (paraIter.hasNext()) {
						if (paraIter.next().getName().equals(toUpdateName[i])) paraContained = true;
					}
					if (!paraContained) {
						Parameter para = new Parameter(toUpdateName[i],toUpdateValue[i],b);
						r.addParameter(para);
					}
				}
				
			}
		}

		controller.saveComponent(p, r);
		
		model.addAttribute("message",msg);
		if (rType.equals("TEMPLATE")) {
			model.addAttribute("linkRef","/editTemplate?project="+project+"&tRefID="+rRefID);
		} else {
			model.addAttribute("linkRef","/editRule?project="+project+"&rule="+rRefID+"&type="+rType);
		}
		model.addAttribute("linkPro","/projectOverview?project="+project);
		
		return "confirmation";
	}
	
	@RequestMapping(value="/confirmDeleteRef", method={RequestMethod.GET})
	public String confirmDeleteRef(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String sourceRefID,
			@RequestParam String sourceType,
			@RequestParam String RefID,
			@RequestParam String Type
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
			
		Component c = null;
		switch (Type) {
			case "GROUP": c = new Group(RefID); break;
			case "CONCEPT": c = new Concept(RefID); break;
			case "CONSTRAINT": c = new Constraint(RefID); break;
			case "TEMPLATE": c = new QueryTemplate(RefID); break;
			default: throw new IllegalArgumentException("no suitable reference type submitted, maybe you called this site manually or an transmission error occured.");
		}
		
		model.addAttribute("sourceRefID", sourceRefID);
		model.addAttribute("sourceType", sourceType);
		model.addAttribute("project", p);
		model.addAttribute("rule", c);
		
		return "confirmationDeleteRef";
	}
	
	@RequestMapping(value="/DeleteRef", method={RequestMethod.POST, RequestMethod.GET})
	public String DeleteRef(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String sourceRefID,
			@RequestParam String sourceType,
			@RequestParam String RefID,
			@RequestParam String Type
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		
		Component source = null;
		switch (sourceType) {
			case "GROUP": source = new Group(sourceRefID); break;
			case "CONCEPT": source = new Concept(sourceRefID); break;
			case "CONSTRAINT": source = new Constraint(sourceRefID); break;
			default: throw new IllegalArgumentException("Error with source Type. Please try again.");
		}
		source = controller.getComponent(p,source);
		if (source == null) throw new IllegalArgumentException("Source component not found.");
		
		Component c = null;
		switch (Type) {
			case "GROUP": c = new Group(RefID); break;
			case "CONCEPT": c = new Concept(RefID); break;
			case "CONSTRAINT": c = new Constraint(RefID); break;
			case "TEMPLATE": c = new QueryTemplate(RefID); break;
			default: throw new IllegalArgumentException("no suitable reference type submitted, maybe you called this site manually or an transmission error occured.");
		}
		c = controller.getComponent(p,c);
		if (c != null) {
			source.deleteReference(c);
			controller.saveComponent(p, source);
		}
		
		String msg = "The reference "+c.getRefID()+" inside the "+sourceType+" "+ sourceRefID +" was successfully deleted!";
		switch (sourceType) {
			case "GROUP": model.addAttribute("linkRef","/editGroup?project="+project+"&group="+sourceRefID); break;
			case "CONCEPT":
			case "CONSTRAINT": model.addAttribute("linkRef","/editRule?project="+project+"&rule="+sourceRefID+"&type="+sourceType); break;
		}		
		model.addAttribute("message",msg);
		model.addAttribute("linkPro","/projectOverview?project="+project);
		
		return "confirmation";
	}
	
	@RequestMapping(value="/confirmDeleteRule", method={RequestMethod.POST, RequestMethod.GET})
	public String confirmDeleteRule(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String rRefID,
			@RequestParam String rType
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
			
		Component r;
		switch (rType) {
		case "CONCEPT":  r = controller.getConcept(p, rRefID); break;
		case "CONSTRAINT": r = controller.getConstraint(p, rRefID); break;
		default: throw new IllegalArgumentException("Supplied type needs to be concept or constraint!");
		}
		if (r == null) throw new IllegalArgumentException("Rule " + rRefID + " does not exist in project " + project);
		
		//get all Nodes, which would be orphaned
		Set<Component> orphaned = controller.getSingleReferencedNodes(p, r);
		
		model.addAttribute("project", p);
		model.addAttribute("rule", r);
		model.addAttribute("orphaned",orphaned);
		
		return "confirmationDeleteRule";
	}
	
	@RequestMapping(value="/DeleteRule", method={RequestMethod.POST, RequestMethod.GET})
	public String DeleteRule(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String rRefID,
			@RequestParam String rType
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
			
		Component r;
		switch (rType) {
		case "CONCEPT":  r = controller.getConcept(p, rRefID); break;
		case "CONSTRAINT": r = controller.getConstraint(p, rRefID); break;
		default: throw new IllegalArgumentException("Supplied type needs to be concept or constraint!");
		}
		if (r == null) throw new IllegalArgumentException("Rule " + rRefID + " does not exist in project " + project);
		
		controller.deleteComponent(p, r);
		
		String msg = "The group "+r.getRefID()+" was successfully deleted!";

		model.addAttribute("message",msg);
		model.addAttribute("linkRef","");
		model.addAttribute("linkPro","/projectOverview?project="+project);
		
		return "confirmation";
	}
	
	
	
	
	
/*
 ********************************************************************************************************* 
 *							GROUP
 ********************************************************************************************************* 
 */
	
	@RequestMapping(value= "/isComponentNameAvailable", method = {RequestMethod.POST,RequestMethod.GET })
	public @ResponseBody Boolean isComponentNameAvailable(
			@CurrentUser RegistredUser regUser,
			@RequestParam(value = "projectName", required = false) String projectName, 
			@RequestParam(value = "cName", required = false) String desiredComponentName, 
			@RequestParam(value = "cType", required = false) String componentType) {
		Project project = controller.getProject(projectName);
		if (project == null) throw new IllegalArgumentException("Project-name " + projectName + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, project)) throw new IllegalArgumentException("you are not allowed to do this!");
			
		
		switch (componentType) {
			case ("GROUP"): {
				if (controller.getGroup(project, desiredComponentName) == null) {
					return true;
				}
				break;
			}
	
			case ("CONCEPT"): {
				if (controller.getConcept(project, desiredComponentName) == null) {
					return true;
				}
				break;
			}
	
			case ("CONSTRAINT"): {
				if (controller.getConstraint(project, desiredComponentName) == null) {
					return true;
				}
				break;
			}
			case ("TEMPLATE"): {
				if (controller.getTemplate(project, desiredComponentName) == null) {
					return true;
				}
				break;
			}

		}
		
			return false;
		
	}
	
	
	@RequestMapping(value="/createGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String createGroup(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam(required=true) String project
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
			
		model.addAttribute("project", p);
		model.addAttribute("group", new Group(""));
		model.addAttribute("taglist", "");
		model.addAttribute("downstram", new HashSet<Component>());
		model.addAttribute("upstream", new HashSet<Component>());
		model.addAttribute("orphaned", new HashSet<Component>());
		model.addAttribute("createGroup", new Boolean(true));
		
		return "editGroup";
	}
	
	@RequestMapping(value="/editGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String editGroup(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam(required=true) String project,
			@RequestParam(required=true) String group,
			@RequestParam(required=false, defaultValue="") String component,
			@RequestParam(required=false, defaultValue="") String type
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
				taglist += iter.next() + ",";
			}
		}
		
		//we misuse the id-field for the override check-box.
		Map<Integer,String> optseverity = g.getOptSeverity();
		Set<Component> tempSet = new HashSet<>(downstream); //we need a copy of the set, to iterate and change items at the same time
		Iterator<Component> iter = tempSet.iterator();
		while (iter.hasNext()) {
			Component temp = iter.next();
			if (optseverity.containsKey(temp.getId().intValue())) {
				//update the component inside the set
				downstream.remove(temp);
				String s = optseverity.get(temp.getId().intValue());
				Long l = new Long (2);
				//on the first position is a zero or one stored to remember check-box decision
				if (s.charAt(0) == '1') l = new Long(1);
				if (s.charAt(0) == '0') l = new Long(0);
				temp.setId(l);
				temp.setOverwriteSeverity(s.substring(1));
				downstream.add(temp);
			}
		}
		
		model.addAttribute("project", p);
		model.addAttribute("group", g);
		model.addAttribute("taglist", taglist);
		model.addAttribute("downstram", downstream);
		model.addAttribute("upstream", upstream);
		model.addAttribute("orphaned", orphaned);
		model.addAttribute("createGroup", new Boolean(false));
		
		if (regUser == null || !usr.workingOn(regUser, p)) return "guesteditGroup";
		else return "editGroup";
	}
	
	@RequestMapping(value="/saveGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String saveGroup(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String gOldID,
			@RequestParam String gRefID,
			@RequestParam String gTaglist
			) {
		
		String msg = " was successfully updated.";
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
			
		if (gRefID.length()<3) throw new IllegalArgumentException("ReferenceID to short");
		Group g = controller.getGroup(p, gOldID);

		//check if there is a group with the same ID:
		Group temp = controller.getGroup(p, gRefID);
		
		if (g==null) {
			//Group isnt existing, create a new one
			if (temp != null) throw new IllegalArgumentException("Group with this ID already exist!");
			g = new Group(gRefID);
			msg = " was successfully created";
		} else {
			//check if old and new refID are identical
			if (!gRefID.equals(gOldID)) {
				if (temp != null) throw new IllegalArgumentException("Group with this ID already exist!");
				g.setRefID(gRefID);
			}
		}
		
		String[] tags = gTaglist.split(",");
		Set<String> tagSet = new HashSet<>(); //use a temporary set to exclude doubles
		for (int i = 0; i < tags.length; i++) {
			//no tags shorter then 1 char, and no spaces. 
			String tempString = tags[i].replace(" ", "");
			if (tempString.length() > 1) tagSet.add(tempString);
		}
		List<String> tagList = new ArrayList<String>(tagSet);
		g.setTags(tagList);
		
		controller.saveComponent(p, g);
		
		model.addAttribute("message", "The group " + msg);
		model.addAttribute("linkRef","/editGroup?project="+project+"&group="+gRefID);
		model.addAttribute("linkPro","/projectOverview?project="+project);
		
		//we come so far, so no exception was thrown
		return "confirmation";
	}
	
	@RequestMapping(value="/referenceGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String referenceGroup(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String gRefID,
			@RequestParam String newRefID,
			@RequestParam String newType,
			@RequestParam String newSeverity
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		
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
		
		return "confirmation";
	}
	
	@RequestMapping(value="/udpateSeverity", method={RequestMethod.POST, RequestMethod.GET})
	public String updateSeverity(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String gRefID,
			@RequestParam(value = "toUpdateSev") String[] toUpdateSev,
			@RequestParam(value = "toUpdateRefID") String[] toUpdateRefID,
			@RequestParam(value = "toUpdateOverride", required = false, defaultValue="") String[] toUpdateOverride,
			@RequestParam(value = "toUpdateType") String[] toUpdateType
			) {
	
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		
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
				//add a leading zero for unchecked override or a one to override the severity
				Boolean b = false;
				if (toUpdateOverride.length>0) for (int j=0; j < toUpdateOverride.length; j++) if (toUpdateOverride[j].equals(toUpdateRefID[i])) b=true;
				g.addReference(c, (b) ? "1"+toUpdateSev[i] : "0"+toUpdateSev[i]);
			}
		}
		controller.saveComponent(p, g);

		model.addAttribute("message",msg);
		model.addAttribute("linkRef","/editGroup?project="+project+"&group="+gRefID);
		model.addAttribute("linkPro","/projectOverview?project="+project);
		

		return "confirmation";
	}
	
	@RequestMapping(value="/confirmDeleteGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String confirmDeleteGroup(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String gRefID
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		
		Group g = controller.getGroup(p, gRefID);
		if (g==null) throw new IllegalArgumentException("Group not found!");
		
		//get all Nodes, which would be orphaned
		Set<Component> orphaned = controller.getSingleReferencedNodes(p, g);
		
		model.addAttribute("project", p);
		model.addAttribute("group", g);
		model.addAttribute("orphaned",orphaned);
		
		return "confirmationDelete";
	}

	@RequestMapping(value="/DeleteGroup", method={RequestMethod.POST, RequestMethod.GET})
	public String DeleteGroup(
			Model model,
			@CurrentUser RegistredUser regUser,
			@RequestParam String project,
			@RequestParam String gRefID
			) {
		
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		
		Group g = controller.getGroup(p, gRefID);
		if (g==null) throw new IllegalArgumentException("Group not found!");
		
		controller.deleteComponent(p, g);
		
		String msg = "The group "+g.getRefID()+" was successfully deleted!";

		model.addAttribute("message",msg);
		model.addAttribute("linkRef","");
		model.addAttribute("linkPro","/projectOverview?project="+project);
		
		return "confirmation";
	}
	
	
	
	
/*
 ********************************************************************************************************* 
 *							TEMPLATE
 ********************************************************************************************************* 
 */
	
	@RequestMapping(value = "/createTemplate")
	public String createTemplate(
			Model model, 
			@CurrentUser RegistredUser regUser,
			@RequestParam String project
			) {
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		
		QueryTemplate template = new QueryTemplate("");
		template.setDescription("");
		template.setCypher("");
		
		model.addAttribute("project",p);
		model.addAttribute("template", template);
		model.addAttribute("parameters", new HashSet<Parameter>());
		model.addAttribute("createTemplate", new Boolean(true));
		
		return "editTemplate";
	}

	@RequestMapping(value="/editTemplate")
	public String editTemplate(
			Model model, 
			@CurrentUser RegistredUser regUser,
			@RequestParam String project, 
			@RequestParam String tRefID
			) {
	
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		
		QueryTemplate template = controller.getTemplate(p, tRefID);
		if (template==null) throw new IllegalArgumentException("Template-RefID " + tRefID + " invalid, Template not existent");
		
		String taglist = "";
		if (template.getTags() != null) {
			Iterator<String> iter = template.getTags().iterator();
			while (iter.hasNext()) {
				taglist += iter.next() + ",";
			}
		}
		
		Set<Parameter> parameters = template.getParameters();
		Set<Component> upstream = controller.getReferencingComponents(p, template);
		
		model.addAttribute("project", p);
		model.addAttribute("template", template);
		model.addAttribute("taglist", taglist);
		model.addAttribute("parameters", parameters);
		model.addAttribute("upstream", upstream);
		model.addAttribute("createTemplate", new Boolean(false));
		if (regUser == null || !usr.workingOn(regUser, p)) return "guesteditTemplate";
		else return "editTemplate";
	}
	
	@RequestMapping(value="/saveTemplate")
	public String saveTemplate(
			Model model, 
			@CurrentUser RegistredUser regUser,
			@RequestParam String project, 
			@RequestParam String tOldID, 
			@RequestParam String tRefID, 
			@RequestParam String tDescr, 
			@RequestParam String tCyph, 
			@RequestParam String tTaglist
			) {
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		if (tRefID.length()<3) throw new IllegalArgumentException("ReferenceID to short");
			QueryTemplate template = controller.getTemplate(p, tOldID);
			QueryTemplate existing = controller.getTemplate(p, tRefID);
			if (template == null) {
				//Template isn't existing, create a new one
				if (existing != null) throw new IllegalArgumentException("Template with this ID already exist!");
				template = new QueryTemplate(tRefID); 
		} else {
			//check if old and new refID are identical, if not check if there is another Component with same ID
			if (!tRefID.equals(tOldID))	{
				if (existing != null) throw new IllegalArgumentException("Rule with this ID already exist!");
				template.setRefID(tRefID);
			}
		}
		String[] tags = tTaglist.split(",");
		Set<String> tagSet = new HashSet<>(); //use a temporary set to exclude doubles
		for (int i = 0; i < tags.length; i++) {
			//no tags shorter then 1 char, and no spaces. 
			String tempTag = tags[i].replace(" ", "");
			if (tempTag.length() > 1) tagSet.add(tempTag);
		}
		List<String> tagList = new ArrayList<String>(tagSet);
		template.setTags(tagList);
		template.setDescription(tDescr);
		template.setCypher(tCyph);
		controller.saveComponent(p, template);
	
		String msg = "";
		if(tOldID.equals("")) msg ="Successfully created new Template " +tRefID+ ".";
		else msg ="Successfully changed Template to " +tRefID+ ".";
		
		model.addAttribute("message",msg);
		model.addAttribute("linkRef","/editTemplate?project="+project+"&tRefID="+tRefID);
		model.addAttribute("linkPro","/projectOverview?project="+project);
		
		return "confirmation";
	}

	@RequestMapping(value ="/confirmationDeleteTemplate", method={RequestMethod.POST, RequestMethod.GET})
	public String confirmDeleteProject(
			Model model, 
			@CurrentUser RegistredUser regUser,
			@RequestParam String  project, 
			@RequestParam String tRefID
			){
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		
		QueryTemplate template = controller.getTemplate(p, tRefID);
		if (template==null) throw new IllegalArgumentException("Template-RefID " + template + " invalid, Template not existent");
		
		model.addAttribute("project", p);
		model.addAttribute("template", tRefID);

		return "confirmationDeleteTemplate";
	}
	
	@RequestMapping(value ="/deleteTemplate", method={RequestMethod.POST, RequestMethod.GET})
	public String deleteTemplate(
			@RequestParam String  project, 
			@RequestParam String tRefID,
			@CurrentUser RegistredUser regUser
			){
		Project p = controller.getProject(project);
		if (p == null) throw new IllegalArgumentException("Project-name " + project + " invalid, Project not existent");
		if (regUser == null || !usr.workingOn(regUser, p)) throw new IllegalArgumentException("you are not allowed to do this!");
		
		QueryTemplate template = controller.getTemplate(p, tRefID);
		if (template==null) throw new IllegalArgumentException("Template-RefID " + template + " invalid, Template not existent");
		
		controller.deleteComponent(p, template);
		
		return "redirect:projects";
	}
}
