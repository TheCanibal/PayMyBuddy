package com.PayMyBuddy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.PayMyBuddy.controller.BuddyController;
import com.PayMyBuddy.controller.LoginController;
import com.PayMyBuddy.controller.RegistrationController;
import com.PayMyBuddy.controller.TransactionController;

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
