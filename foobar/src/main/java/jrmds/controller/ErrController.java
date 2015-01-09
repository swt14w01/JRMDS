package jrmds.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrController implements ErrorController {
	
	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@RequestMapping(value="/error")
	public ModelAndView ArgErrorHandler(Exception e) {
		ModelAndView model = new ModelAndView("errorArgument");
		
		model.addObject("exception", e.getMessage());
		
		return model;
	}
	
	@ExceptionHandler(Exception.class)
	@RequestMapping(value="/error")
	public ModelAndView defaultErrorHandler(Exception e) {
		ModelAndView model = new ModelAndView("error2");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		e.printStackTrace(pw);
		
		String exception = sw.getBuffer().toString();
		
		model.addObject("exception", exception);
		//model.addAttribute("StackTrace", StackTrace);
		//model.addAttribute("test", StackTrace.length());
		
		return model;
	}
}

