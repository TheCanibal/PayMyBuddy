package com.PayMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.PayMyBuddy.model.Buddy;
import com.PayMyBuddy.model.Transaction;
import com.PayMyBuddy.service.BuddyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Controller
public class BuddyController {

    @Autowired
    private BuddyService buddyService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Display the home page with some object with ModelAndView
     * 
     * @return home page with some objects added
     */
    @GetMapping("/")
    public ModelAndView home() {
	// Obtains the currently authenticated principal, or an authentication request
	// token
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// Connected user to recover
	Buddy currentBuddy = new Buddy();
	// if someone is authenticated, recover the connected user
	if (auth != null) {
	    SecurityContext ctx = SecurityContextHolder.getContext();
	    // Obtains the currently authenticated principal
	    Object principal = ctx.getAuthentication().getPrincipal();
	    // Obtains user's email to find it in database
	    String currentEmail = ((UserDetails) principal).getUsername();
	    // Load the user with his email
	    currentBuddy = buddyService.getBuddyByEmail(currentEmail);
	}
	// Create a ModelAndView to insert objects in home.html
	ModelAndView mav = new ModelAndView("home");
	mav.addObject("buddy", new Buddy());
	mav.addObject("friends", currentBuddy.getFriends());
	mav.addObject("newTransaction", new Transaction());
	mav.addObject("transactions", currentBuddy.getTransactions());
	mav.addObject("sold", currentBuddy.getSold());
	return mav;
    }

    /**
     * Display the login page
     * 
     * @return login.html file to load
     */
    @GetMapping("/login")
    public String login() {
	return "login";
    }

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
     * Logout method
     * 
     * @param request
     * @param response
     * @return redirection to the login page with a successfull logout message
     */
    @PostMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	if (auth != null) {
	    new SecurityContextLogoutHandler().logout(request, response, auth);
	}
	return "redirect:/login?logout";
    }

    /**
     * Add friend to be allowed to make transactions
     * 
     * @param buddy user to add as a friend
     * @return redirection to home page or error page
     */
    @PostMapping("/addFriend")
    @Transactional
    public String addFriend(@ModelAttribute Buddy buddy) {
	// Obtains the currently authenticated principal
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// Connected user to recover
	Buddy currentBuddy = new Buddy();
	// User to add as friend
	Buddy buddyToAdd = buddyService.getBuddyByEmail(buddy.getEmail());
	if (auth != null) {
	    SecurityContext ctx = SecurityContextHolder.getContext();
	    Object principal = ctx.getAuthentication().getPrincipal();
	    String currentEmail = ((UserDetails) principal).getUsername();
	    currentBuddy = buddyService.getBuddyByEmail(currentEmail);
	}
	// if user to add exists and is in database and isn't already a friend of
	// connected user
	if (buddyToAdd != null && buddyService.getBuddies().contains(buddyToAdd)
		&& !(currentBuddy.getFriends().contains(buddyToAdd))) {
	    // Add user to add as a friend of connected user
	    currentBuddy.addFriend(buddyToAdd);
	    return "redirect:/";
	} else {
	    return "redirect:/?errorAdded";
	}
    }

    @PostMapping("/register")
    @Transactional
    public String register(@ModelAttribute Buddy newBuddy) {
	newBuddy.setRole("USER");
	newBuddy.setPassword(passwordEncoder.encode(newBuddy.getPassword()));
	System.out.println(newBuddy.getEmail());
	System.out.println(newBuddy.getFirstName());
	System.out.println(newBuddy.getLastName());
	System.out.println(newBuddy.getSold());
	System.out.println(newBuddy.getPassword());
	buddyService.addBuddy(newBuddy);
	return "redirect:/login?successRegister";
    }
}
