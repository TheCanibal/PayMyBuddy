package com.PayMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.PayMyBuddy.model.Buddy;
import com.PayMyBuddy.service.BuddyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Controller
public class BuddyController {

    @Autowired
    private BuddyService buddyService;

    @GetMapping("/")
    public ModelAndView home() {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	Buddy currentBuddy = new Buddy();
	if (auth != null) {
	    SecurityContext ctx = SecurityContextHolder.getContext();
	    Object principal = ctx.getAuthentication().getPrincipal();
	    String currentEmail = ((UserDetails) principal).getUsername();
	    currentBuddy = buddyService.getBuddyByEmail(currentEmail);
	}
	ModelAndView mav = new ModelAndView("home");
	mav.addObject("buddy", new Buddy());
	mav.addObject("friends", currentBuddy.getFriends());
	return mav;
    }

    @GetMapping("/list")
    public ModelAndView getAllUsers() {
	ModelAndView mav = new ModelAndView("list-users");
	mav.addObject("users", buddyService.getBuddies());
	return mav;
    }

    @GetMapping("/login")
    public String login() {
	return "login";
    }

    @PostMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	if (auth != null) {
	    new SecurityContextLogoutHandler().logout(request, response, auth);
	}
	return "redirect:/login?logout";
    }

    @PostMapping("/addFriend")
    @Transactional
    public String addFriend(@ModelAttribute Buddy buddy) {

	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	Buddy currentBuddy = new Buddy();
	Buddy buddyToAdd = buddyService.getBuddyByEmail(buddy.getEmail());
	if (auth != null) {
	    SecurityContext ctx = SecurityContextHolder.getContext();
	    Object principal = ctx.getAuthentication().getPrincipal();
	    String currentEmail = ((UserDetails) principal).getUsername();
	    currentBuddy = buddyService.getBuddyByEmail(currentEmail);
	}
	if (buddyToAdd != null && buddyService.getBuddies().contains(buddyToAdd)
		&& !(currentBuddy.getFriends().contains(buddyToAdd))) {
	    currentBuddy.addFriend(buddyToAdd);
	    return "redirect:/";
	} else {
	    return "redirect:/?error";
	}

    }
}
