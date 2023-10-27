package com.PayMyBuddy.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Buddy {
    @Id
    @Column(name = "email")
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "add_friend", joinColumns = @JoinColumn(name = "email"), inverseJoinColumns = @JoinColumn(name = "email_friend"))
    private List<Buddy> friends = new ArrayList<Buddy>();

    @ManyToMany(mappedBy = "buddies", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Transaction> transactions = new ArrayList<Transaction>();

    public void addFriend(Buddy buddy) {
	try {
	    if (!(friends.contains(buddy))) {
		friends.add(buddy);
	    } else {
		throw new IllegalArgumentException("Cette personne est déjà dans votre liste d'ami");
	    }
	} catch (IllegalArgumentException iae) {

	}
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

    public List<Transaction> getTransactions() {
	return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
	this.transactions = transactions;
    }

}
