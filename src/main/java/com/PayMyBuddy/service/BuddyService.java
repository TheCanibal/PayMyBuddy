package com.PayMyBuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PayMyBuddy.model.Buddy;
import com.PayMyBuddy.repository.BuddyRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
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
    public Buddy addBuddy(Buddy buddy) {
	return buddyRepository.save(buddy);
    }

    /**
     * Update a user in the database
     * 
     * @param buddy buddy to update in the database
     * @return the updated entity
     */
    public Buddy updateBuddy(Buddy buddy) {
	return buddyRepository.save(buddy);
    }

}
