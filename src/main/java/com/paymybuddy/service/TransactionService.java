package com.paymybuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.paymybuddy.exception.ErrorAmountException;
import com.paymybuddy.exception.ErrorTransactionException;
import com.paymybuddy.exception.NotEnoughMoneyException;
import com.paymybuddy.model.Buddy;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BuddyService buddyService;

    private static final double FEE = 0.005;

    /**
     * Get a list of all transactions in database
     * 
     * @return a list of all transactions
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Execute payment to a friend
     * 
     * @param buddy          friend to send money
     * @param newTransaction create a new transaction to register it and display it
     */
    public void pay(Buddy buddy, Transaction newTransaction) throws Exception {
        // Connected user to recover
        Buddy currentBuddy = buddyService.getCurrentBuddy();
        // Friend to pay
        Buddy buddyToPay = buddyService.getBuddyByEmail(buddy.getEmail());
        // Amount to transfer with interest rounded with 2 digits after dot
        double feeToDeduce = (Math.round(newTransaction.getAmount() * FEE * 100.0) / 100.0);
        // Amount recieved by friend
        double amount = newTransaction.getAmount();
        // If friend to pay is not null and is in database and if sold is superior or
        // equal to transaction amount and transaction has description and amount to
        // transfer >= 1
        if (currentBuddy != null && buddyToPay != null && buddyService.getBuddies().contains(buddyToPay)
                && newTransaction.getDescription() != null
                && newTransaction.getAmount() >= 1) {
            // update user's sold
            if ((currentBuddy.getSold() - (amount + feeToDeduce)) >= 0) {
                currentBuddy.setSold(currentBuddy.getSold() - (amount + feeToDeduce));
            } else {
                throw new NotEnoughMoneyException();
            }
            // update user in database
            buddyService.updateBuddy(currentBuddy);
            // update friend's sold
            buddyToPay.setSold(buddyToPay.getSold() + amount);
            // update friend in database
            buddyService.updateBuddy(buddyToPay);
            // set the fee
            newTransaction.setFee(feeToDeduce);
            // add transactions to the transaction's lists (Sender and Reciever)
            currentBuddy.addTransactionSend(newTransaction);
            buddyToPay.addTransactionRecieve(newTransaction);
        } else {
            throw new ErrorTransactionException();
        }
    }

    /**
     * Add money to balance if it is possible
     * 
     * @param sold sold to display in profile page
     * @return redirection to profile page or error page
     * @throws Exception
     */
    public void addMoneyBalance(Buddy sold) throws Exception {
        // Connected user to recover
        Buddy currentBuddy = buddyService.getCurrentBuddy();
        // if someone is authenticated, recover the connected user
        double roundSold = Math.round(sold.getSold() * 100.0) / 100.0;
        // if sold > 0 and <= 20 000 and user is connected
        if (roundSold > 0 && roundSold <= 20000 && currentBuddy != null) {
            // Set new sold and update user's sold in DB else show error message
            currentBuddy.setSold(roundSold + currentBuddy.getSold());
            buddyService.updateBuddy(currentBuddy);
        } else {
            throw new ErrorAmountException();
        }
    }

    /**
     * Add money to bank if it is possible
     * 
     * @param sold sold to display in profile page
     * @return redirection to profile page or error page
     * @throws Exception
     */
    @PostMapping("/addMoneyBank")
    public void addMoneyBank(@ModelAttribute Buddy sold) throws Exception {
        // Connected user to recover
        Buddy currentBuddy = buddyService.getCurrentBuddy();
        // Round sold to 2 decimals
        double roundSold = Math.round(sold.getSold() * 100.0) / 100.0;
        // if user is connected and sold to send is < to user's sold and sold to send >
        // 0 and <= 20 000
        if (currentBuddy != null && currentBuddy.getSold() - roundSold > 0 && roundSold > 0 && roundSold <= 20000) {
            // set new sold and update in DB else show error message
            currentBuddy.setSold(currentBuddy.getSold() - roundSold);
            buddyService.updateBuddy(currentBuddy);
        } else {
            throw new ErrorAmountException();
        }
    }
}
