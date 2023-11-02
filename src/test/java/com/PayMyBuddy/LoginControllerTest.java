package com.PayMyBuddy;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
	mockMvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    public void userLoginTest() throws Exception {
	mockMvc.perform(formLogin("/login").user("email", "jeandupont@mail.fr").password("123456"))
		.andExpect(authenticated());
    }

    @Test
    public void userWrongLoginTest() throws Exception {
	mockMvc.perform(formLogin("/login").user("email", "jeandupont@mail.fr").password("wrong"))
		.andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void shouldReturnLoginPageWithSuccessLogoutMessage() throws Exception {
	mockMvc.perform(logout()).andDo(print()).andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/login?logout"));
    }
}
