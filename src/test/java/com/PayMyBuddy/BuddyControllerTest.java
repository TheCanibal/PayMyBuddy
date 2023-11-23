package com.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.model.Buddy;
import com.paymybuddy.model.BuddyDetails;
import com.paymybuddy.service.BuddyService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BuddyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BuddyService buddyService;

    private BuddyDetails buddyDetails;

    @Test
    @WithMockUser(value = "jeandupont@mail.fr")
    public void shouldReturnHomePage() throws Exception {
        buddyDetails = new BuddyDetails(buddyService.getBuddyByEmail("jeandupont@mail.fr"));
        mockMvc.perform(get("/").with(user(buddyDetails))).andExpect(status().isOk()).andExpect(view().name("transfer"))
                .andExpect(model().attributeExists("buddy", "friends", "newTransaction", "transactions"))
                .andExpect(model().attribute("sold", 50.0));
    }

    @Test
    @WithMockUser
    public void shouldReturnLoginPageHomeView() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void shouldReturnProfilePage() throws Exception {
        mockMvc.perform(get("/profile.html")).andExpect(status().isOk()).andExpect(view().name("profile.html"));
    }

    @Test
    @WithMockUser
    public void shouldReturnLoginPageProfileView() throws Exception {
        mockMvc.perform(get("/profile.html")).andExpect(status().isOk()).andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void addFriendInFriendListTest() throws Exception {
        Buddy jeandupont = buddyService.getBuddyByEmail("jeandupont@mail.fr");
        int numberOfFriends = jeandupont.getFriends().size();

        mockMvc.perform(post("/addFriend").param("email", "gilbertlarousse@mail.fr").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/?friendAdded"));

        assertEquals(numberOfFriends + 1, jeandupont.getFriends().size());
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void addFriendInFriendListWithUnknownUserTest() throws Exception {

        mockMvc.perform(post("/addFriend").param("email", "unknownUser@mail.fr").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/?addFriendError"));
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void addFriendWhoIsAlreadyFriendInFriendListTest() throws Exception {

        mockMvc.perform(post("/addFriend").param("email", "michelmartin@mail.fr").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/?addFriendError"));
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void addNullFriendInFriendListTest() throws Exception {

        mockMvc.perform(post("/addFriend").with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/?addFriendError"));
    }

    @Test
    @WithMockUser
    public void addFriendInUnknownUserFriendListTest() throws Exception {

        mockMvc.perform(post("/addFriend").param("email", "gilbertlarousse@mail.fr").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/?addFriendError"));
    }

    @Test
    @WithMockUser
    public void addNullFriendInUnknownUserFriendListTest() throws Exception {

        mockMvc.perform(post("/addFriend").with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/?addFriendError"));
    }

    @Test
    @WithMockUser
    public void addUnknownFriendInUnknownUserFriendListTest() throws Exception {

        mockMvc.perform(post("/addFriend").param("email", "unknownUser@mail.fr").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/?addFriendError"));
    }

}
