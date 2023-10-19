package com.PayMyBuddy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	return http.authorizeHttpRequests(auth -> {
	    auth.requestMatchers("/").hasRole("USER");
	    auth.requestMatchers("/list").hasRole("USER");
	    auth.requestMatchers("/addFriend").hasRole("USER");
	    auth.requestMatchers("/?error").hasRole("USER");
	    auth.requestMatchers("/resources/**", "/css/**").permitAll().anyRequest().authenticated();
	}).formLogin(form -> {
	    form.loginPage("/login").permitAll();
	    form.usernameParameter("email");
	    form.passwordParameter("password");
	    form.defaultSuccessUrl("/");
	    form.failureUrl("/login?error=true").permitAll();
	}).logout(logout -> {
	    logout.logoutUrl("/logout").permitAll();
	    logout.logoutSuccessUrl("/login?logout").invalidateHttpSession(true).deleteCookies("JSESSIONID");

	}).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder)
	    throws Exception {
	AuthenticationManagerBuilder authenticationManagerBuilder = http
		.getSharedObject(AuthenticationManagerBuilder.class);
	authenticationManagerBuilder.userDetailsService(customUserDetailsService)
		.passwordEncoder(bCryptPasswordEncoder);
	return authenticationManagerBuilder.build();
    }
}
