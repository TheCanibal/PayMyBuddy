package com.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.service.BuddyService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BuddyService buddyService;

    @Test
    public void shouldReturnRegistrationPage() throws Exception {
	mockMvc.perform(get("/registration.html")).andExpect(status().isOk())
		.andExpect(view().name("registration.html")).andExpect(model().attributeExists("newBuddy"));
    }

    @Test
    public void registerTest() throws Exception {
	int numberOfUsersInDatabase = buddyService.getBuddies().size();

	mockMvc.perform(post("/register").param("email", "test@mail.fr").param("firstName", "Test")
		.param("lastName", "Test").param("password", "123456").with(csrf()))
		.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/login?successRegister"));

	assertEquals(buddyService.getBuddies().size(), numberOfUsersInDatabase + 1);
    }

    @Test
    public void registerWithoutEmailTest() throws Exception {

	mockMvc.perform(post("/register").param("firstName", "Test").param("lastName", "Test")
		.param("password", "123456").with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/registration.html?errorEmail"));

    }

    @Test
    public void registerWithoutFirstNameTest() throws Exception {

	mockMvc.perform(post("/register").param("email", "test@mail.fr").param("lastName", "Test")
		.param("password", "123456").with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/registration.html?errorFirstName"));

    }

    @Test
    public void registerWithoutLastNameTest() throws Exception {

	mockMvc.perform(post("/register").param("email", "test@mail.fr").param("firstName", "Test")
		.param("password", "123456").with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/registration.html?errorLastName"));

    }

    @Test
    public void registerWithoutPasswordTest() throws Exception {

	mockMvc.perform(post("/register").param("email", "test@mail.fr").param("firstName", "Test")
		.param("lastName", "Test").with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/registration.html?errorPassword"));

    }

    @Test
    public void registerWithWrongEmail() throws Exception {
	mockMvc.perform(post("/register").param("email", "testmail.fr").param("firstName", "Test")
		.param("lastName", "Test").param("password", "123456").with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/registration.html?errorEmail"));

    }

}
