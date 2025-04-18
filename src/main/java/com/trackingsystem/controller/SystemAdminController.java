package com.trackingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trackingsystem.dto.CreateAdminRequest;
import com.trackingsystem.models.SystemAdmin;
import com.trackingsystem.repository.SystemAdminRepository;
import com.trackingsystem.service.SystemAdminService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(path="/api/systemAdmin")
public class SystemAdminController {
	
	@Autowired
	private SystemAdminRepository adminRepository;
	
	@Autowired
    private SystemAdminService adminService;

	@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public SystemAdmin createAdmin(@RequestBody CreateAdminRequest request) {
        return adminService.createAdmin(request);
    }
	
    /*
	@PostMapping(path = "/login")
	public String login(@RequestParam("username") String username,
	                    @RequestParam("password") String password,
	                    RedirectAttributes redirectAttributes,
	                    HttpSession session) {
	    // Retrieve admin from the database
	    SystemAdmin admin = adminRepository.findByUsername(username);

	    if (admin != null && admin.getPassword().equals(password)) {
	        // Store admin details in the session
	        session.setAttribute("loggedInAdmin", admin);
	        redirectAttributes.addFlashAttribute("successMessage", "Login successful!");
	        return "adminIndex"; // Redirect to the admin dashboard
	    } else {
	        redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password!");
	        return "adminLogin"; // Redirect back to the login page with an alert
	    }
	}
	*/

    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping(path = "/logout")
	public String logout(HttpSession session) {
	    // Invalidate the session to log the user out
	    session.invalidate();
	    return "homeIndex"; // Redirect to login page
	}
    
	@GetMapping("/login")
	public String adminLoginPage() {
	    System.out.println("✅ /api/systemAdmin/login was called");
	    return "adminLogin";
	}

	@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/adminDash")
    public String dashboardPage() {
        return "adminIndex";
    }

}
