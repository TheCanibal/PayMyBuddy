package com.PayMyBuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PayMyBuddy.model.Transaction;
import com.PayMyBuddy.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Get a list of all transactions in database
     * 
     * @return a list of all transactions
     */
    public List<Transaction> getAllTransactions() {
	return transactionRepository.findAll();
    }

    /**
     * Add a new transaction to the database
     * 
     * @param transaction transaction to add in database
     * @return the saved entity
     */
    public Transaction addTransaction(Transaction transaction) {
	return transactionRepository.save(transaction);
    }
}
