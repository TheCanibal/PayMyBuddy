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

    public List<Buddy> getBuddies() {
	return buddyRepository.findAll();
    }

    public Buddy getBuddyByEmail(String email) {
	return buddyRepository.findByEmail(email);
    }

    public Buddy addBuddy(Buddy buddy) {
	return buddyRepository.save(buddy);
    }

}
