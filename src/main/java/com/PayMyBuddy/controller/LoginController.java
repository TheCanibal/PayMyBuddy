package com.PayMyBuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * Display the login page
     * 
     * @return login.html file to load
     */
    @GetMapping("/login")
    public String login(Model model) {
	return "login";
    }

}
