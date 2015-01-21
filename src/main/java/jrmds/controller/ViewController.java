package jrmds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class ViewController extends WebMvcConfigurerAdapter{

	@RequestMapping(value = "/signin", method = { RequestMethod.GET, RequestMethod.POST })
	public String loginPage() {
		return "login";
	} 
}
