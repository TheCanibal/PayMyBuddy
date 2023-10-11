package com.PayMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.PayMyBuddy.repository.BuddyRepository;

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
}
