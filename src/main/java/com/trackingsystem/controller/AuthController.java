package com.trackingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.trackingsystem.models.VehicleOwner;
import com.trackingsystem.repository.VehicleOwnerRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(path="/auth")
public class AuthController {
	
	
	@Autowired
	VehicleOwnerRepository ownerRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;

	public AuthController(VehicleOwnerRepository ownerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/loginRequest")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String rawPassword, // User input
                        Model model,
                        HttpSession session) {

        System.out.println("Login attempt with email: " + email);

        VehicleOwner owner = ownerRepository.findByEmail(email);

        if (owner == null) {
            System.out.println("No owner found with email: " + email);
            model.addAttribute("error", "Invalid username or password.");
            return "vehicleOwnerLogin";
        }

        System.out.println("Owner found: " + owner.getOwnerFullName());

        // Compare hashed password
        if (passwordEncoder.matches(rawPassword, owner.getPassword())) {
            session.setAttribute("loggedInOwner", owner);
            return "index"; // Redirect to dashboard/home page
        } else {
            System.out.println("Password mismatch for email: " + email);
            model.addAttribute("error", "Invalid username or password.");
            return "vehicleOwnerLogin";
        }
    }

    

	
	 @GetMapping("/login")
	    public String showLoginPage(HttpSession session) {
	        // If the user is already logged in, redirect to the homepage
	        if (session.getAttribute("loggedInOwner") != null) {
	            return "index";  // Redirect to the homepage
	        }
	        return "homeIndex";  // Otherwise, show the login page
	    }


	 @GetMapping(path = "/logout")
	 public String logout(HttpSession session) {
	     // Invalidate the session to remove all session attributes
	     if (session != null) {
	         session.invalidate(); // Ends the session
	     }

	     // Redirect to the login page after logout
	     return "redirect:/auth/login";
	 }

    
}
