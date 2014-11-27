package jrmds.controller;

import java.util.ArrayList;
import java.util.List;

import jrmds.main.JrmdsManagement;
import jrmds.main.RuleRepository;
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
	@Autowired
	public RuleRepository ruleRepository;

	List<Component> autocompleteList;

	@RequestMapping(value = "/getAutoCompleteSuggestions", method = RequestMethod.GET)
	public @ResponseBody List<Component> getAutoCompleteSuggestions(
			@RequestParam String tagName) {
		autocompleteList = new ArrayList<Component>();
		for (Component person : controller.getAllComponents()) {
			if (person.getRefID().toLowerCase().contains(tagName))

				autocompleteList.add(person);
		}

		return autocompleteList;

	}

	/* @RequestMapping(value="/projectOverview", method = ) */

}
