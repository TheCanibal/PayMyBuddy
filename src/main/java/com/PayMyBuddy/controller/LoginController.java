package com.PayMyBuddy.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
}
