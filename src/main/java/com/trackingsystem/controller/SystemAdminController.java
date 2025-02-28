package com.trackingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trackingsystem.models.SystemAdmin;
import com.trackingsystem.repository.SystemAdminRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(path="/api/systemAdmin")
public class SystemAdminController {
	
	@Autowired
	private SystemAdminRepository adminRepository;
	
	@PostMapping(path = "/loginRequest")
	public String login(@RequestParam("username") String username,
	                    @RequestParam("password") String password,
	                    Model model,
	                    HttpSession session) {
	    // Retrieve admin from the database
	    SystemAdmin admin = adminRepository.findByUsername(username);

	    if (admin != null && admin.getPassword().equals(password)) {
	        // Store admin details in the session
	        session.setAttribute("loggedInAdmin", admin);
	        return "adminIndex"; // Redirect to admin dashboard
	    } else {
	        model.addAttribute("error", "Invalid username or password.");
	        return "adminLogin"; // Reload login page with error message
	    }
	}

	@GetMapping(path = "/logout")
	public String logout(HttpSession session) {
	    // Invalidate the session to log the user out
	    session.invalidate();
	    return "homeIndex"; // Redirect to login page
	}
    
    @GetMapping("/login")
    public String login() {
        return "adminLogin";
    }
    
    @GetMapping("/adminDash")
    public String dashboardPage() {
        return "adminIndex";
    }

}
