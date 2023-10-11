package com.PayMyBuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PayMyBuddy.model.Buddy;

@Repository
public interface BuddyRepository extends JpaRepository<Buddy, String> {
    public Buddy findByEmail(String email);
}
