package com.paymybuddy.model;

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
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private double amount;

    @Column(name = "fee")
    private double fee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email_sender", referencedColumnName = "email")
    private Buddy buddySender = null;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email_reciever", referencedColumnName = "email")
    private Buddy buddyReciever = null;

    public Transaction() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
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
