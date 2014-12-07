package jrmds.controller;

import java.util.HashSet;
import java.util.Set;

import jrmds.main.JrmdsManagement;
import jrmds.model.Component;
import jrmds.model.ComponentType;
import jrmds.model.SearchRequest;
import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	Set<Component> autocompleteList;

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
		model.addAttribute("searchRequest", searchRequest);
		return "searchResults";
	}

	@RequestMapping(value = "/processSearchRequest", method = { RequestMethod.POST })
	public String processSearchRequest(SearchRequest searchRequest) {
		Set<Component> resultSet = new HashSet<>();
		Set<Component> componentInventory = controller.getAllComponents();
		String searchTerm = searchRequest.getSearchTerm();

		for (Component component : componentInventory) {
			if (component.getRefID().toLowerCase().contains(searchTerm)) {
				if ((component.getType().equals(ComponentType.GROUP) && searchRequest.getIncludeGroups())
					|| (component.getType().equals(ComponentType.CONCEPT) && searchRequest.getIncludeConcepts()) 
					|| (component.getType().equals(ComponentType.CONSTRAINT) && searchRequest.getIncludeConstraints())
					|| (component.getType().equals(ComponentType.TEMPLATE) && searchRequest.getIncludeQueryTemplates())) {

					resultSet.add(component);
					System.out.println(component.getRefID() + "COMPOMENT TYPE : " + component.getType());
				}
			}
		}



		return "searchResults";
	}

}
