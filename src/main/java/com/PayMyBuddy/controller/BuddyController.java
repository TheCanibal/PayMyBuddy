package com.PayMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.PayMyBuddy.model.Buddy;
import com.PayMyBuddy.model.Transaction;
import com.PayMyBuddy.service.BuddyService;

import jakarta.transaction.Transactional;

@Controller
public class BuddyController {

    @Autowired
    private BuddyService buddyService;

    /**
     * Display the home page with some object with ModelAndView
     * 
     * @return home page with some objects added or login page with error
     */
    @GetMapping("/")
    public ModelAndView transfer() {
	// Connected user to recover
	Buddy currentBuddy = buddyService.BuddyIsConnected();
	if (currentBuddy != null) {
	    // Create a ModelAndView to insert objects in transfer.html
	    ModelAndView mav = new ModelAndView("transfer");
	    mav.addObject("buddy", new Buddy());
	    mav.addObject("friends", currentBuddy.getFriends());
	    mav.addObject("newTransaction", new Transaction());
	    mav.addObject("transactions", currentBuddy.getTransactions());
	    mav.addObject("sold", currentBuddy.getSold());
	    return mav;
	} else {
	    // if not authenticated, return login page
	    ModelAndView mav = new ModelAndView("login");
	    return mav;
	}

    }

    @GetMapping("/profile.html")
    public ModelAndView profile() {
	// Connected user to recover
	Buddy currentBuddy = buddyService.BuddyIsConnected();
	// if someone is authenticated, recover the connected user
	if (currentBuddy != null) {
	    // Model and view for profile.html and insert object
	    ModelAndView mav = new ModelAndView("profile.html");
	    mav.addObject("buddy", currentBuddy);
	    mav.addObject("sold", new Buddy());
	    return mav;
	} else {
	    // if not authenticated, return login page
	    ModelAndView mav = new ModelAndView("login");
	    return mav;
	}

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
	// Connected user to recover
	Buddy currentBuddy = buddyService.BuddyIsConnected();
	// User to add as friend
	Buddy buddyToAdd = buddyService.getBuddyByEmail(buddy.getEmail());
	// if user to add exists and is in database and isn't already a friend of
	// connected user
	if (currentBuddy != null && buddyToAdd != null && buddyService.getBuddies().contains(buddyToAdd)
		&& !(currentBuddy.getFriends().contains(buddyToAdd))) {
	    // Add user to add as a friend of connected user
	    currentBuddy.addFriend(buddyToAdd);
	    return "redirect:/";
	} else {
	    return "redirect:/?errorAdded";
	}
    }

}
