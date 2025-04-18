package com.trackingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trackingsystem.models.VehicleOwner;
import com.trackingsystem.repository.VehicleOwnerRepository;
import com.trackingsystem.service.OwnerService;

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
	
	@Autowired
    private OwnerService service;

	@PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        String response = service.resetPassword(request);
        return response.equals("Password updated successfully!") 
                ? ResponseEntity.ok(response) 
                : ResponseEntity.badRequest().body(response);
    }

    /*
	@PostMapping(path = "/loginRequest")
	public String login(@RequestParam("email") String email,
	                    @RequestParam("password") String rawPassword,
	                    HttpSession session,
	                    RedirectAttributes redirectAttributes) {

	    System.out.println("Login attempt with email: " + email);

	    VehicleOwner owner = ownerRepository.findByEmail(email);

	    if (owner == null) {
	        System.out.println("No owner found with email: " + email);
	        redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
	        return "redirect:/auth/login"; // Redirect back to login page
	    }

	    System.out.println("Owner found: " + owner.getOwnerFullName());

	    if (passwordEncoder.matches(rawPassword, owner.getPassword())) {
	        session.setAttribute("loggedInOwner", owner);
	        redirectAttributes.addFlashAttribute("success", "Login successful!");
	        return "redirect:/app/dashboard"; // Redirect to home page
	    } else {
	        System.out.println("Password mismatch for email: " + email);
	        redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
	        return "redirect:/auth/login"; // Redirect back to login page
	    }
	}
	
	*/


	
	@GetMapping("/loginPage")
	public String showLoginPage(HttpSession session) {
	    if (session.getAttribute("loggedInOwner") != null) {
	        return "index"; // This will look for templates/index.html
	    }
	    return "homeIndex"; // This will look for templates/homeIndex.html
	}

	 @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
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
