package com.PayMyBuddy.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.PayMyBuddy.model.Buddy;
import com.PayMyBuddy.repository.BuddyRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private BuddyRepository buddyRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	Buddy buddy = buddyRepository.findByEmail(email);
	return new User(buddy.getEmail(), buddy.getPassword(), getGrantedAuthorities(buddy.getRole()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
	return authorities;
    }
}
