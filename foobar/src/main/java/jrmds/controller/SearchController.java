package jrmds.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import jrmds.main.JrmdsManagement;
import jrmds.model.Component;
import jrmds.model.ComponentType;
import jrmds.model.SearchRequest;
import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class SearchController extends WebMvcConfigurerAdapter {
	@Autowired
	private JrmdsManagement controller;
	@Autowired
	private UserManagement usr;

	Set<Component> autocompleteList = new HashSet<>();

	Set<Component> resultSet = new HashSet<>();

	Map<Component, String> resultConcepts = new HashMap<>();
	Map<Component, String> resultGroups = new HashMap<>();
	Map<Component, String> resultConstraints = new HashMap<>();
	Map<Component, String> resultQueryTemplates = new HashMap<>();

	String searchTerm;

	int numberOfGroups = 0;
	int numberOfConcepts = 0;
	int numberOfConstraints = 0;
	int numberOfTemplates = 0;

	@RequestMapping(value = "/getAutoCompleteSuggestions", method = RequestMethod.GET)
	public @ResponseBody Set<Component> getAutoCompleteSuggestions(@RequestParam String tagName) {
		autocompleteList = new HashSet<Component>();
		for (Component component : controller.getAllComponents()) {
			if (component.getRefID().toLowerCase().contains(tagName))

				autocompleteList.add(component);
		}

		return autocompleteList;

	}

	@RequestMapping(value = "/searchResults", method = { RequestMethod.GET })
	public String searchResults(SearchRequest searchRequest, Model model) {
		return "searchResults";
	}

	@RequestMapping(value = "/processSearchRequest", method = { RequestMethod.GET, RequestMethod.POST })
	public String processSearchRequest(@Valid SearchRequest searchRequest, Model model, BindingResult bindingResult, @RequestParam String searchType,
			@RequestParam(required = false) String tagTerm) {

		if (searchType.equals("default")) {
			searchRequest.setDefault();
		}

		if (bindingResult.hasErrors()) {
			System.out.println("rw");
			return "";
		}

		resetResultSets();

		Set<Component> componentInventory = controller.getAllComponents();

		if (tagTerm != null) {
			searchTerm = tagTerm;
		} else {
			searchTerm = searchRequest.getSearchTerm();
		}

		for (Component component : componentInventory) {
			if (component.getRefID().toLowerCase().contains(searchTerm) || component.getTags().contains(searchTerm) || component.getDescription().contains(searchTerm)) {
				if ((component.getType().equals(ComponentType.GROUP) && searchRequest.getIncludeGroups())
						|| (component.getType().equals(ComponentType.CONCEPT) && searchRequest.getIncludeConcepts())
						|| (component.getType().equals(ComponentType.CONSTRAINT) && searchRequest.getIncludeConstraints())
						|| (component.getType().equals(ComponentType.TEMPLATE) && searchRequest.getIncludeQueryTemplates())) {

					resultSet.add(component);
					switch (component.getType()) {
					case GROUP:
						resultGroups.put(component, controller.getComponentAssociatedProject(component).getName());
						break;
					case CONCEPT:
						resultConcepts.put(component, controller.getComponentAssociatedProject(component).getName());
						break;
					case CONSTRAINT:
						resultConstraints.put(component, controller.getComponentAssociatedProject(component).getName());
						break;
					case PARAMETER:
						break;
					case TEMPLATE:
						resultQueryTemplates.put(component, controller.getComponentAssociatedProject(component).getName());
						break;
					default:
						break;
					}

				}
			}
		}

		model.addAttribute("searchRequest", searchRequest);
		model.addAttribute("searchTerm", searchTerm);
		model.addAttribute("numberOfResults", resultSet.size());
		model.addAttribute("numberOfGroups", resultGroups.size());
		model.addAttribute("numberOfConcepts", resultConcepts.size());
		model.addAttribute("numberOfConstraints", resultConstraints.size());
		model.addAttribute("numberOfTemplates", resultQueryTemplates.size());

		model.addAttribute("resultGroups", resultGroups);
		model.addAttribute("resultConcepts", resultConcepts);
		model.addAttribute("resultConstraints", resultConstraints);
		model.addAttribute("resultQueryTemplates", resultQueryTemplates);

		return "searchResults";
	}

	private void resetResultSets() {
		resultSet.clear();
		resultGroups.clear();
		resultConcepts.clear();
		resultConstraints.clear();
		resultQueryTemplates.clear();
	}

}
