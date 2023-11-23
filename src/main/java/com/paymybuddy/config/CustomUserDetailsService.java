package com.paymybuddy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.Buddy;
import com.paymybuddy.model.BuddyDetails;
import com.paymybuddy.repository.BuddyRepository;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private BuddyRepository buddyRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Buddy buddy = buddyRepository.findByEmail(email);
        if (buddy == null) {
            throw new UsernameNotFoundException(email);
        }
        return new BuddyDetails(buddy);
    }

}
