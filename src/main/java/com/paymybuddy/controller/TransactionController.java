package com.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.paymybuddy.exception.ErrorAmountException;
import com.paymybuddy.exception.ErrorTransactionException;
import com.paymybuddy.exception.NotEnoughMoneyException;
import com.paymybuddy.model.Buddy;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.service.TransactionService;

import jakarta.transaction.Transactional;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Execute payment to a friend
     * 
     * @param buddy          friend to send money
     * @param newTransaction create a new transaction to register it and display it
     * @return redirection to home page or error page
     * @throws Exception
     */
    @PostMapping("/pay")
    @Transactional
    public String pay(@ModelAttribute Buddy buddy, @ModelAttribute Transaction newTransaction) throws Exception {
        try {
            transactionService.pay(buddy, newTransaction);
        } catch (ErrorTransactionException ete) {
            return "redirect:/?errorTransaction";
        } catch (NotEnoughMoneyException neme) {
            return "redirect:/?errorNotEnoughMoney";
        }
        return "redirect:/?successfullTransaction";
    }

    /**
     * Add money to balance if it is possible
     * 
     * @param sold sold to display in profile page
     * @return redirection to profile page or error page
     * @throws Exception
     */
    @PostMapping("/addMoneyBalance")
    public String addMoneyBalance(@ModelAttribute Buddy sold) throws Exception {
        try {
            transactionService.addMoneyBalance(sold);
        } catch (ErrorAmountException eae) {
            return "redirect:/profile.html?errorAmount";
        }
        return "redirect:/profile.html";
    }

    /**
     * Add money to bank if it is possible
     * 
     * @param sold sold to display in profile page
     * @return redirection to profile page or error page
     * @throws Exception
     */
    @PostMapping("/addMoneyBank")
    public String addMoneyBank(@ModelAttribute Buddy sold) throws Exception {
        try {
            transactionService.addMoneyBank(sold);
        } catch (ErrorAmountException eae) {
            return "redirect:/profile.html?errorAmount";
        }
        return "redirect:/profile.html";
    }
}
