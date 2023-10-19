package com.PayMyBuddy.model;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "utilisateur")
public class Buddy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "sold")
    private int sold;

    @Column(name = "role")
    private String role;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ajout_ami", joinColumns = @JoinColumn(name = "email"), inverseJoinColumns = @JoinColumn(name = "email_ami"))
    private List<Buddy> friends = new ArrayList<Buddy>();

    @ManyToMany(mappedBy = "friends", cascade = CascadeType.ALL)
    private List<Buddy> friendsOf = new ArrayList<Buddy>();

    public void addFriend(Buddy buddy) {
	friends.add(buddy);
	buddy.getFriendsOf().add(this);
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

    public int getSold() {
	return sold;
    }

    public void setSold(int sold) {
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

    public List<Buddy> getFriendsOf() {
	return friendsOf;
    }

    public void setFriendsOf(List<Buddy> friendsOf) {
	this.friendsOf = friendsOf;
    }
}
