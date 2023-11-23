package com.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.model.Buddy;
import com.paymybuddy.service.BuddyService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BuddyService buddyService;

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void payTest() throws Exception {
        Buddy jeandupont = buddyService.getBuddyByEmail("jeandupont@mail.fr");
        Buddy michelmartin = buddyService.getBuddyByEmail("michelmartin@mail.fr");
        double jeandupontSold = jeandupont.getSold();
        double michelmartinSold = michelmartin.getSold();
        assertEquals(50.0, jeandupontSold);
        assertEquals(101.0, michelmartinSold);

        mockMvc.perform(post("/pay").param("email", "michelmartin@mail.fr").param("description", "Birthday")
                .param("amount", "20.0").with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?successfullTransaction"));

        assertEquals(29.9, jeandupont.getSold());
        assertEquals(121.0, michelmartin.getSold());
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void payWithoutFriendEmailTest() throws Exception {
        mockMvc.perform(post("/pay").param("description", "Birthday").param("amount", "20.0").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/?errorTransaction"));
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void payWithoutDescriptionTest() throws Exception {
        mockMvc.perform(post("/pay").param("email", "michelmartin@mail.fr").param("amount", "20.0").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/?errorTransaction"));
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void payWithoutAmountTest() throws Exception {
        mockMvc.perform(
                post("/pay").param("email", "michelmartin@mail.fr").param("description", "Birthday").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/?errorTransaction"));
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void payWithTooHighAmountTest() throws Exception {
        mockMvc.perform(post("/pay").param("email", "michelmartin@mail.fr").param("description", "Birthday")
                .param("amount", "9999999.0").with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?errorTransaction"));
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void payWithWrongFriendEmailTest() throws Exception {
        mockMvc.perform(post("/pay").param("email", "unknow@mail.fr").param("description", "Birthday")
                .param("amount", "20.0").with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?errorTransaction"));
    }

    @Test
    @WithMockUser
    public void payWithUnknownUserTest() throws Exception {
        mockMvc.perform(post("/pay").param("email", "michelmartin@mail.fr").param("description", "Birthday")
                .param("amount", "20.0").with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?errorTransaction"));
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void addMoneyToBalanceTest() throws Exception {
        Buddy jeandupont = buddyService.getBuddyByEmail("jeandupont@mail.fr");
        double jeandupontSold = jeandupont.getSold();
        assertEquals(50.0, jeandupontSold);

        mockMvc.perform(post("/addMoneyBalance").param("sold", "100.0").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/profile.html"));

        assertEquals(150.0, jeandupont.getSold());
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void addMoneyToBalanceBelowZeroTest() throws Exception {

        mockMvc.perform(post("/addMoneyBalance").param("sold", "-50").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/profile.html?errorAmount"));

    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void addMoneyToBalanceAboveTwentyThousandTest() throws Exception {

        mockMvc.perform(post("/addMoneyBalance").param("sold", "50000").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/profile.html?errorAmount"));

    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void addMoneyToBankTest() throws Exception {
        Buddy jeandupont = buddyService.getBuddyByEmail("jeandupont@mail.fr");
        double jeandupontSold = jeandupont.getSold();
        assertEquals(50.0, jeandupontSold);

        mockMvc.perform(post("/addMoneyBank").param("sold", "30.0").with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile.html"));

        assertEquals(20.0, jeandupont.getSold());
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void addTooMuchMoneyToBankTest() throws Exception {

        mockMvc.perform(post("/addMoneyBank").param("sold", "100").with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile.html?errorAmount"));
    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void addMoneyToBankBelowZeroTest() throws Exception {

        mockMvc.perform(post("/addMoneyBank").param("sold", "-50").with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile.html?errorAmount"));

    }

    @Test
    @WithMockUser(username = "jeandupont@mail.fr")
    public void addMoneyToBankAboveTwentyThousandTest() throws Exception {

        mockMvc.perform(post("/addMoneyBank").param("sold", "50000").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/profile.html?errorAmount"));

    }

}
