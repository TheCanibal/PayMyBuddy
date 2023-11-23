package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.Buddy;

@Repository
public interface BuddyRepository extends JpaRepository<Buddy, Integer> {

    /**
     * Get user with his email
     * 
     * @param email user's email
     * @return user who owns the mail
     */
    public Buddy findByEmail(String email);
}
