package com.PayMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.PayMyBuddy.model.Buddy;
import com.PayMyBuddy.model.Transaction;
import com.PayMyBuddy.service.BuddyService;

import jakarta.transaction.Transactional;

@Controller
public class TransactionController {

    @Autowired
    private BuddyService buddyService;

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
	// Amount to transfer with interest rounded with 2 digits after dot
	double amountInterestRoundedToMinus = (Math.round(newTransaction.getAmount() * 1.005 * 100.0) / 100.0);
	// Amount recieved by friend
	double amount = newTransaction.getAmount();
	// If friend to pay is not null and is in database and if sold is superior or
	// equal to transaction amount and transaction has description and amount to
	// transfer >= 1
	if (currentBuddy != null && buddyToPay != null && buddyService.getBuddies().contains(buddyToPay)
		&& currentBuddy.getSold() >= amount && newTransaction.getDescription() != null
		&& newTransaction.getAmount() >= 1) {
	    // update user's sold
	    if ((currentBuddy.getSold() - amountInterestRoundedToMinus) >= 0) {
		currentBuddy.setSold(currentBuddy.getSold() - amountInterestRoundedToMinus);
	    } else {
		return "redirect:/?errorNotEnoughMoney";
	    }
	    // update user in database
	    buddyService.updateBuddy(currentBuddy);
	    // update friend's sold
	    buddyToPay.setSold(buddyToPay.getSold() + amount);
	    // update friend in database
	    buddyService.updateBuddy(buddyToPay);
	    // add transactions to the transaction's lists (Sender and Reciever
	    currentBuddy.addTransactionSend(newTransaction);
	    buddyToPay.addTransactionRecieve(newTransaction);

	    return "redirect:/?successfullTransaction";
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
	// if sold > 0 and <= 20 000 and user is connected
	if (roundSold > 0 && roundSold <= 20000 && currentBuddy != null) {
	    // Set new sold and update user's sold in DB else show error message
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
	// if user is connected and sold to send is < to user's sold and sold to send >
	// 0 and <= 20 000
	if (currentBuddy != null && currentBuddy.getSold() - roundSold > 0 && roundSold > 0 && roundSold <= 20000) {
	    // set new sold and update in DB else show error message
	    currentBuddy.setSold(currentBuddy.getSold() - roundSold);
	    buddyService.updateBuddy(currentBuddy);
	    return "redirect:/profile.html";
	} else {
	    return "redirect:/profile.html?errorAmount";
	}
    }
}
