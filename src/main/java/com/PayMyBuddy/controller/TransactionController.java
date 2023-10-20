package com.PayMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.PayMyBuddy.model.Buddy;
import com.PayMyBuddy.model.Transaction;
import com.PayMyBuddy.service.BuddyService;
import com.PayMyBuddy.service.TransactionService;

import jakarta.transaction.Transactional;

@Controller
public class TransactionController {

    @Autowired
    private BuddyService buddyService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/pay")
    @Transactional
    public String pay(@ModelAttribute Buddy buddy, @ModelAttribute Transaction newTransaction) {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	Buddy currentBuddy = new Buddy();
	Buddy buddyToPay = buddyService.getBuddyByEmail(buddy.getEmail());
	System.out.println(buddyToPay.getFirstName());
	int amount = newTransaction.getAmount();
	if (auth != null) {
	    SecurityContext ctx = SecurityContextHolder.getContext();
	    Object principal = ctx.getAuthentication().getPrincipal();
	    String currentEmail = ((UserDetails) principal).getUsername();
	    currentBuddy = buddyService.getBuddyByEmail(currentEmail);
	}
	if (buddyToPay != null && buddyService.getBuddies().contains(buddyToPay)
		&& currentBuddy.getSold() >= newTransaction.getAmount()) {
	    newTransaction.setFirstName(buddyToPay.getFirstName());
	    newTransaction.setLastName(buddyToPay.getLastName());
	    transactionService.addTransaction(newTransaction);
	    currentBuddy.setSold(currentBuddy.getSold() - amount);
	    buddyService.updateBuddy(currentBuddy);
	    buddyToPay.setSold(buddyToPay.getSold() + amount);
	    buddyService.updateBuddy(buddyToPay);
	    newTransaction.addBuddies(currentBuddy);
	    System.out.println(newTransaction.getAmount() + "cond");
	    System.out.println(newTransaction.getDescription() + "cond");
	    return "redirect:/";
	} else {
	    return "redirect:/?error";
	}
    }
}
