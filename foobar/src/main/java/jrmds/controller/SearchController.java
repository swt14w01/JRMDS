package jrmds.controller;

import java.util.HashSet;
import java.util.Set;

import jrmds.main.JrmdsManagement;
import jrmds.model.Component;
import jrmds.user.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
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

	/* @RequestMapping(value="/projectOverview", method = ) */

}
