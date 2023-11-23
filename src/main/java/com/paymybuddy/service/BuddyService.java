package com.paymybuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.paymybuddy.exception.AddFriendErrorException;
import com.paymybuddy.model.Buddy;
import com.paymybuddy.repository.BuddyRepository;

@Service
public class BuddyService {

    @Autowired
    private BuddyRepository buddyRepository;

    /**
     * Get all users from database
     * 
     * @return List of all users from database
     */
    public List<Buddy> getBuddies() {
        return buddyRepository.findAll();
    }

    /**
     * Get user with his email
     * 
     * @param email user's email
     * @return user who owns the mail
     */
    public Buddy getBuddyByEmail(String email) {
        return buddyRepository.findByEmail(email);
    }

    /**
     * Add a user to the database
     * 
     * @param buddy buddy to add to the database
     * @return the saved entity
     */
    public void addBuddy(Buddy buddy) {
        buddyRepository.save(buddy);
    }

    /**
     * Update a user in the database
     * 
     * @param buddy buddy to update in the database
     * @return the updated entity
     */
    public void updateBuddy(Buddy buddy) {
        buddyRepository.save(buddy);
    }

    /**
     * Verify if user is connected
     * 
     * @return connected user
     */
    public Buddy getCurrentBuddy() {
        // Obtains the currently authenticated principal, or an authentication request
        // token
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Connected user to recover
        Buddy currentBuddy;
        // if someone is authenticated, recover the connected user else return null
        if (auth != null) {
            SecurityContext ctx = SecurityContextHolder.getContext();
            // Obtains the currently authenticated principal
            Object principal = ctx.getAuthentication().getPrincipal();
            // Obtains user's email to find it in database
            String currentEmail = ((UserDetails) principal).getUsername();
            // Load the user with his email
            currentBuddy = buddyRepository.findByEmail(currentEmail);
            return currentBuddy;
        } else {
            return null;
        }
    }

    /**
     * Add friend to be allowed to make transactions
     * 
     * @param buddy user to add as a friend
     * @return redirection to home page or error page
     */
    public void addFriend(@ModelAttribute Buddy buddy) throws Exception {
        // Connected user to recover
        Buddy currentBuddy = getCurrentBuddy();
        // User to add as friend
        Buddy buddyToAdd = getBuddyByEmail(buddy.getEmail());
        // if user to add exists and is in database and isn't already a friend of
        // connected user
        if (currentBuddy != null && buddyToAdd != null && getBuddies().contains(buddyToAdd)
                && !(currentBuddy.getFriends().contains(buddyToAdd))) {
            // Add user to add as a friend of connected user
            currentBuddy.addFriend(buddyToAdd);
        } else {
            throw new AddFriendErrorException();
        }
    }

}
