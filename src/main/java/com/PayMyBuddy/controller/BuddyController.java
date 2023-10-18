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
import com.PayMyBuddy.repository.BuddyRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BuddyController {

    @Autowired
    private BuddyRepository buddyRepository;

    @GetMapping("/")
    public ModelAndView home() {
	ModelAndView mav = new ModelAndView("home");
	mav.addObject("buddy", new Buddy());
	return mav;
    }

    @GetMapping("/list")
    public ModelAndView getAllUsers() {
	ModelAndView mav = new ModelAndView("list-users");
	mav.addObject("users", buddyRepository.findAll());
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
    public String addFriend(@ModelAttribute String email) {

	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	Buddy currentBuddy = new Buddy();
	Buddy buddyToAdd = buddyRepository.findByEmail(email);
	if (auth != null && buddyToAdd != null) {
	    SecurityContext ctx = SecurityContextHolder.getContext();
	    Object principal = ctx.getAuthentication().getPrincipal();
	    String currentEmail = ((UserDetails) principal).getUsername();
	    currentBuddy = buddyRepository.findByEmail(currentEmail);
	    currentBuddy.addFriend(buddyToAdd);

	}
	return "redirect:/";
    }
}
