package jrmds.controller;

import java.util.HashSet;
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

	int numberOfGroups = 0;
	int numberOfConcepts = 0;
	int numberOfConstraints = 0;
	int numberOfTemplates = 0;

	@RequestMapping(value = "/getAutoCompleteSuggestions", method = RequestMethod.GET)
	public @ResponseBody Set<Component> getAutoCompleteSuggestions(
			@RequestParam String tagName) {
		autocompleteList = new HashSet<Component>();
		for (Component component : controller.getAllComponents()) {
			if (component.getRefID().toLowerCase().contains(tagName))

				autocompleteList.add(component);
		}

		return autocompleteList;

	}

	@RequestMapping(value = "/advancedSearch")
	public String advancedSearch() {
		return "advancedSearch";
	}

	@RequestMapping(value = "/searchResults", method = { RequestMethod.GET })
	public String searchResults(SearchRequest searchRequest, Model model) {
		return "searchResults";
	}

	@RequestMapping(value = "/processSearchRequest", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String processSearchRequest(@Valid SearchRequest searchRequest,
			Model model, BindingResult bindingResult, @RequestParam String searchType) {

		if(searchType.equals("default")) {
			searchRequest.setDefault();
		}
		

		if (bindingResult.hasErrors()) {
			System.out.println("rw");
			return "";
		}

		resultSet.clear();
		numberOfGroups = 0;
		numberOfConcepts = 0;
		numberOfConstraints = 0;
		numberOfTemplates = 0;
		Set<Component> componentInventory = controller.getAllComponents();
		String searchTerm = searchRequest.getSearchTerm();

		for (Component component : componentInventory) {
			if (component.getRefID().toLowerCase().contains(searchTerm) || component.getTags().contains(searchTerm)) {
				if ((component.getType().equals(ComponentType.GROUP) && searchRequest
						.getIncludeGroups())
						|| (component.getType().equals(ComponentType.CONCEPT) && searchRequest
								.getIncludeConcepts())
						|| (component.getType()
								.equals(ComponentType.CONSTRAINT) && searchRequest
								.getIncludeConstraints())
						|| (component.getType().equals(ComponentType.TEMPLATE) && searchRequest
								.getIncludeQueryTemplates())) {

					resultSet.add(component);
					System.out.println(component.toString());
					switch (component.getType()) {
					case GROUP:
						numberOfGroups++;
						break;
					case CONCEPT:
						numberOfConcepts++;
						break;
					case CONSTRAINT:
						numberOfConstraints++;
						break;
					case PARAMETER:
						break;
					case TEMPLATE:
						numberOfTemplates++;
						break;
					default:
						break;
					}

					System.out.println(component.getRefID()
							+ "COMPOMENT TYPE : " + component.getType());
				}
			}
		}

		model.addAttribute("searchRequest", searchRequest);
		model.addAttribute("numberOfResults", resultSet.size());
		model.addAttribute("numberOfGroups", numberOfGroups);
		model.addAttribute("numberOfConcepts", numberOfConcepts);
		model.addAttribute("numberOfConstraints", numberOfConstraints);
		model.addAttribute("numberOfTemplates", numberOfTemplates);

		return "searchResults";
	}

}
