package com.trackingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/app")
public class AppController {
	
	@GetMapping("/homepage")
	public String Homepage() {
		return "homeIndex";
	}
	
	@GetMapping("/dashboard")
	public String Dash() {
		return "index";
	}
	
	

}
