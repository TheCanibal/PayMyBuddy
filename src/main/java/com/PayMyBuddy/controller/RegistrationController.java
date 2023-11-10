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
	// if buddy to register has a valid email
	if (newBuddy.getEmail() != null) {
	    // if email contains @ and user has filled first name, last name and password
	    // field
	    if (newBuddy.getEmail().contains("@") && newBuddy.getFirstName() != null && newBuddy.getLastName() != null
		    && newBuddy.getPassword() != null) {
		// set sold to zero
		newBuddy.setSold(0);
		// set role to user
		newBuddy.setRole("USER");
		// encode password before add to DB
		newBuddy.setPassword(passwordEncoder.encode(newBuddy.getPassword()));
		// add new user to database and return login page with successful register
		// message
		buddyService.addBuddy(newBuddy);
		return "redirect:/login?successRegister";
		// if email doesn't contain @, return error
	    } else if (!(newBuddy.getEmail().contains("@"))) {
		return "redirect:/registration.html?errorEmail";
		// if first name field is empty return error message
	    } else if (newBuddy.getFirstName() == null) {
		return "redirect:/registration.html?errorFirstName";
		// if last name field is empty return error message
	    } else if (newBuddy.getLastName() == null) {
		return "redirect:/registration.html?errorLastName";
		// else return error message for password
	    } else {
		return "redirect:/registration.html?errorPassword";
	    }
	    // else return error message for email
	} else {
	    return "redirect:/registration.html?errorEmail";
	}
    }
}
