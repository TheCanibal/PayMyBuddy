package com.PayMyBuddy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.PayMyBuddy.model.Buddy;
import com.PayMyBuddy.service.BuddyService;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class PayMyBuddyAppApplication implements CommandLineRunner {

    @Autowired
    private BuddyService bs;

    public static void main(String[] args) {
	SpringApplication.run(PayMyBuddyAppApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
	Buddy jean = bs.getBuddyByEmail("jeandupont@mail.fr");
	Buddy gilbert = bs.getBuddyByEmail("gilbertlarousse@mail.fr");
	Buddy michel = bs.getBuddyByEmail("michelmartin@mail.fr");
	System.out.println(jean.getFirstName() + " " + gilbert.getFirstName() + " " + michel.getFirstName());
	bs.getBuddies().forEach(buddy -> System.out.println(buddy.getFirstName()));
	michel.getFriends().forEach(friends -> System.out.println("Ami de Michel : " + friends.getFirstName()));
	michel.getFriendsOf().forEach(friends -> System.out.println("Michel est l'ami de : " + friends.getFirstName()));

    }

}
