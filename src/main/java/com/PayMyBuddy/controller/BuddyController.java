package com.PayMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.PayMyBuddy.repository.BuddyRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BuddyController {

    @Autowired
    private BuddyRepository userRepository;

    @GetMapping("/")
    public String home() {
	return "home";
    }

    @GetMapping("/list")
    public ModelAndView getAllUsers() {
	ModelAndView mav = new ModelAndView("list-users");
	mav.addObject("users", userRepository.findAll());
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
}
