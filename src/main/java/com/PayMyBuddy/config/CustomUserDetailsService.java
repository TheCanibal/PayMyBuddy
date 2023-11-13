package com.PayMyBuddy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.PayMyBuddy.model.Buddy;
import com.PayMyBuddy.model.BuddyDetails;
import com.PayMyBuddy.repository.BuddyRepository;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private BuddyRepository buddyRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	Buddy buddy = buddyRepository.findByEmail(email);
	return new BuddyDetails(buddy);
    }

}
