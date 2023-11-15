package com.PayMyBuddy.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private long id;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private double amount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email_sender")
    private Buddy buddySender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email_reciever")
    private Buddy buddyReciever;

    public Transaction() {
	super();
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public Buddy getBuddySender() {
	return buddySender;
    }

    public void setBuddySender(Buddy buddySender) {
	this.buddySender = buddySender;
    }

    public Buddy getBuddyReciever() {
	return buddyReciever;
    }

    public void setBuddyReciever(Buddy buddyReciever) {
	this.buddyReciever = buddyReciever;
    }

}
