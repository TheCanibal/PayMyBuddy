package com.paymybuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;

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
}
