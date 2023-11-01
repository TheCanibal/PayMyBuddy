package com.PayMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
	// Connected user to recover
	Buddy currentBuddy = buddyService.BuddyIsConnected();
	// Friend to pay
	Buddy buddyToPay = buddyService.getBuddyByEmail(buddy.getEmail());
	// Amount to transfer with interest
	double amountInterestRounded = Math.round(newTransaction.getAmount() * 1.005 * 100.0) / 100.0;
	// Amount recieved by friend
	double amount = newTransaction.getAmount();
	// If friend to pay is not null and is in database and if sold is superior or
	// equal to transaction amount
	if (currentBuddy != null && buddyToPay != null && buddyService.getBuddies().contains(buddyToPay)
		&& currentBuddy.getSold() >= amount) {
	    // set first name and last name to display in transactions
	    newTransaction.setFirstName(buddyToPay.getFirstName());
	    newTransaction.setLastName(buddyToPay.getLastName());
	    // add transaction to database
	    transactionService.addTransaction(newTransaction);
	    // Set a negative amount because user lose money
	    newTransaction.setAmount(-amountInterestRounded);
	    // update user's sold
	    currentBuddy.setSold(currentBuddy.getSold() - amountInterestRounded);
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
	// Connected user to recover
	Buddy currentBuddy = buddyService.BuddyIsConnected();
	// if someone is authenticated, recover the connected user
	double roundSold = Math.round(sold.getSold() * 100.0) / 100.0;
	if (roundSold > 0 && roundSold <= 1000 && currentBuddy != null) {
	    currentBuddy.setSold(roundSold + currentBuddy.getSold());
	    buddyService.updateBuddy(currentBuddy);
	    return "redirect:/profile.html";
	} else {
	    return "redirect:/profile.html?errorAmount";
	}
    }

    @PostMapping("/addMoneyBank")
    public String addMoneyBank(@ModelAttribute Buddy sold) {
	// Connected user to recover
	Buddy currentBuddy = buddyService.BuddyIsConnected();
	// Round sold to 2 decimals
	double roundSold = Math.round(sold.getSold() * 100.0) / 100.0;
	if (currentBuddy != null && currentBuddy.getSold() - roundSold > 0 && roundSold > 0 && roundSold <= 1000) {
	    currentBuddy.setSold(currentBuddy.getSold() - roundSold);
	    buddyService.updateBuddy(currentBuddy);
	    return "redirect:/profile.html";
	} else {
	    return "redirect:/profile.html?errorAmount";
	}
    }
}
