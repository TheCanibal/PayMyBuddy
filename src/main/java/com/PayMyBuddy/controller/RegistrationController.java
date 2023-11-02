package com.PayMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.PayMyBuddy.model.Buddy;
import com.PayMyBuddy.service.BuddyService;

import jakarta.transaction.Transactional;

@Controller
public class RegistrationController {

    @Autowired
    private BuddyService buddyService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Display the registration page
     * 
     * @return registration.html file to load
     */
    @GetMapping("/registration.html")
    public ModelAndView registration() {
	ModelAndView mav = new ModelAndView("registration.html");
	Buddy newBuddy = new Buddy();
	mav.addObject("newBuddy", newBuddy);
	return mav;
    }

    /**
     * Register a new user in database with encoded password
     * 
     * @param newBuddy buddy to register
     * @return redirection to the login page with successful register message
     */
    @PostMapping("/register")
    @Transactional
    public String register(@ModelAttribute Buddy newBuddy) {
	if (newBuddy.getEmail() != null) {
	    if (newBuddy.getEmail().contains("@") && newBuddy.getFirstName() != null && newBuddy.getLastName() != null
		    && newBuddy.getPassword() != null) {
		newBuddy.setSold(0);
		newBuddy.setRole("USER");
		newBuddy.setPassword(passwordEncoder.encode(newBuddy.getPassword()));
		buddyService.addBuddy(newBuddy);
		return "redirect:/login?successRegister";
	    } else if (!(newBuddy.getEmail().contains("@"))) {
		return "redirect:/registration.html?errorEmail";
	    } else if (newBuddy.getFirstName() == null) {
		return "redirect:/registration.html?errorFirstName";
	    } else if (newBuddy.getLastName() == null) {
		return "redirect:/registration.html?errorLastName";
	    } else {
		return "redirect:/registration.html?errorPassword";
	    }
	} else {
	    return "redirect:/registration.html?errorEmail";
	}
    }
}
