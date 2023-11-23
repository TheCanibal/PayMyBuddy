package com.PayMyBuddy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.controller.BuddyController;
import com.paymybuddy.controller.LoginController;
import com.paymybuddy.controller.RegistrationController;
import com.paymybuddy.controller.TransactionController;

@SpringBootTest
class PayMyBuddyAppApplicationTests {

    @Autowired
    private LoginController loginController;

    @Autowired
    private BuddyController buddyController;

    @Autowired
    private TransactionController transactionController;

    @Autowired
    private RegistrationController registrationController;

    @Test
    void contextLoads() {
	assertThat(loginController).isNotNull();
	assertThat(buddyController).isNotNull();
	assertThat(transactionController).isNotNull();
	assertThat(registrationController).isNotNull();
    }

}
