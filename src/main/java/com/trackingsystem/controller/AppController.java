package com.trackingsystem.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
@Controller
@RequestMapping(path="/app")
public class AppController {
	
	@GetMapping("/homepage")
	public String Homepage() {
		return "homeIndex";
	}
	
	
	@GetMapping("/adminIndex")
	public String Admin() {
		return "adminIndex";
	}	
	@GetMapping("/resetForm")
	public String ResetPasswordForm() {
		return "resetPasswordPage";
	}
	
	 @GetMapping("/adminDash")
	    public String dashboardPage() {
	        return "adminIndex";
	    }
	 
	 @GetMapping("/dashboard")
		public String Dash() {
			return "index";
		}
	
}
