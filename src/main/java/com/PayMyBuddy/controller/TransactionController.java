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

    /**
     * Execute payment to a friend
     * 
     * @param buddy          friend to send money
     * @param newTransaction create a new transaction to register it and display it
     * @return redirection to home page or error page
     */
    @PostMapping("/pay")
    @Transactional
    public String pay(@ModelAttribute Buddy buddy, @ModelAttribute Transaction newTransaction) {
	// Obtains the currently authenticated principal
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// Connected user to recover
	Buddy currentBuddy = new Buddy();
	// Friend to pay
	Buddy buddyToPay = buddyService.getBuddyByEmail(buddy.getEmail());
	// Amount to transfer with interest
	double amountInterest = newTransaction.getAmount() * 1.005;
	System.out.println(amountInterest);
	// Amount recieved by friend
	double amount = newTransaction.getAmount();
	if (auth != null) {
	    SecurityContext ctx = SecurityContextHolder.getContext();
	    Object principal = ctx.getAuthentication().getPrincipal();
	    String currentEmail = ((UserDetails) principal).getUsername();
	    currentBuddy = buddyService.getBuddyByEmail(currentEmail);
	}
	// If friend to pay is not null and is in database and if sold is superior or
	// equal to transaction amount
	if (buddyToPay != null && buddyService.getBuddies().contains(buddyToPay) && currentBuddy.getSold() >= amount) {
	    // set first name and last name to display in transactions
	    newTransaction.setFirstName(buddyToPay.getFirstName());
	    newTransaction.setLastName(buddyToPay.getLastName());
	    // add transaction to database
	    transactionService.addTransaction(newTransaction);
	    // Set a negative amount because user lose money
	    newTransaction.setAmount(-amountInterest);
	    // update user's sold
	    currentBuddy.setSold(currentBuddy.getSold() - amountInterest);
	    // update user in database
	    buddyService.updateBuddy(currentBuddy);
	    // update friend's sold
	    buddyToPay.setSold(buddyToPay.getSold() + amount);
	    // update friend in database
	    buddyService.updateBuddy(buddyToPay);
	    // create a new transaction to allow friend that recieve money to see the
	    // transaction information
	    Transaction transactionReverse = new Transaction();
	    // Set the first name and last name of the user who send money
	    transactionReverse.setFirstName(currentBuddy.getFirstName());
	    transactionReverse.setLastName(currentBuddy.getLastName());
	    // Set amount
	    transactionReverse.setAmount(amount);
	    // Set description
	    transactionReverse.setDescription(newTransaction.getDescription());
	    // add transactions to the association table
	    newTransaction.addBuddies(currentBuddy);
	    transactionReverse.addBuddies(buddyToPay);

	    return "redirect:/";
	} else {
	    return "redirect:/?errorTransaction";
	}
    }

    @PostMapping("/addMoneyBalance")
    public String addMoneyBalance(@ModelAttribute Buddy sold) {
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

	currentBuddy.setSold(sold.getSold() + currentBuddy.getSold());
	buddyService.updateBuddy(currentBuddy);

	return "redirect:/profile.html";
    }
}
