package jrmds.controller;

import jrmds.model.SearchRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class ViewController extends WebMvcConfigurerAdapter{

	@RequestMapping(value="/")
	public String index(SearchRequest searchRequest){
		return "index";
	}
	
	
}
