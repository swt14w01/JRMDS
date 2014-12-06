package jrmds.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
public class ErrController implements ErrorController {
	
	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	@ExceptionHandler(Exception.class)
	@RequestMapping(value="/error")
	public String error() {
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		//e.printStackTrace(pw);
		
		String exception = "test";
		
		//model.addAttribute("exception", exception);
		//model.addAttribute("StackTrace", StackTrace);
		//model.addAttribute("test", StackTrace.length());
		
		return "error2";
	}
}
