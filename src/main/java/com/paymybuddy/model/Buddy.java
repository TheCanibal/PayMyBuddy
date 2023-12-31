package com.paymybuddy.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "buddy")
public class Buddy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "sold")
    private double sold;

    @Column(name = "role")
    private String role;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "friend", joinColumns = @JoinColumn(name = "email", referencedColumnName = "email"), inverseJoinColumns = @JoinColumn(name = "email_friend", referencedColumnName = "email"))
    private List<Buddy> friends = new ArrayList<Buddy>();

    @OneToMany(mappedBy = "buddySender", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Transaction> transactionsSend = new ArrayList<>();

    @OneToMany(mappedBy = "buddyReciever", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Transaction> transactionsRecieve = new ArrayList<>();

    public void addFriend(Buddy buddy) {
        if (!(friends.contains(buddy))) {
            friends.add(buddy);
        }

    }

    public void addTransactionSend(Transaction transaction) {
        transactionsSend.add(transaction);
        transaction.setBuddySender(this);
    }

    public void addTransactionRecieve(Transaction transaction) {
        transactionsRecieve.add(transaction);
        transaction.setBuddyReciever(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Buddy() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getSold() {
        return sold;
    }

    public void setSold(double sold) {
        this.sold = sold;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Buddy> getFriends() {
        return friends;
    }

    public void setFriends(List<Buddy> friends) {
        this.friends = friends;
    }

    public List<Transaction> getTransactionsSend() {
        return transactionsSend;
    }

    public void setTransactionsSend(List<Transaction> transactionsSend) {
        this.transactionsSend = transactionsSend;
    }

    public List<Transaction> getTransactionsRecieve() {
        return transactionsRecieve;
    }

    public void setTransactionsRecieve(List<Transaction> transactionsRecieve) {
        this.transactionsRecieve = transactionsRecieve;
    }

}
