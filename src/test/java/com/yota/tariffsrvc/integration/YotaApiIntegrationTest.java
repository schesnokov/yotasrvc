package com.yota.tariffsrvc.integration;

import com.yota.tariffsrvc.App;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
public class YotaApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void checkAppContextAndBeansLoaded() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertNotNull(wac.getBean("accountController"));
    }

    @Test
    public void whenGetAccByIdThenReturnAccPositive() throws Exception {
        this.mockMvc.perform(get("/api/accounts/{accId}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("+79213335671"));
    }

    @Test
    public void changeStatusOfAccPositive() throws Exception {
        this.mockMvc.perform(post("/api/accounts/changestatus/{phoneNumber}", "+79213335672"))
                .andDo(print())
                .andExpect(content().string("true"))
                .andExpect(status().isOk());
    }

    @Test
    public void changeStatusOfAccNegative() throws Exception {
        this.mockMvc.perform(post("/api/accounts/changestatus/{phoneNumber}", "+79213335675"))
                .andDo(print())
                .andExpect(status().reason("No account with this phone +79213335675"));
    }

    @Test
    public void getAvailablePackagesPositive() throws Exception {
        this.mockMvc.perform(get("/api/accounts/available/{phoneNumber}", "+79213335671"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getAvailablePackagesNegative() throws Exception {
        this.mockMvc.perform(get("/api/accounts/available/{phoneNumber}", "+79213335673"))
                .andDo(print())
                .andExpect(status().reason("Account with phone +79213335673 is inactive"));
    }

    @Test
    public void spendMinutesFromExistingAccPositive() throws Exception {
        this.mockMvc.perform(post("/api/accounts/minutes/spend/{phoneNumber}/{minutesAmount}", "+79213335671", "30"))
                .andDo(print())
                .andExpect(content().string("170"))
                .andExpect(status().isOk());
    }

    @Test
    public void spendMinutesFromExistingAccNegativeValue() throws Exception {
        this.mockMvc.perform(post("/api/accounts/minutes/spend/{phoneNumber}/{minutesAmount}", "+79213335671", "-30"))
                .andDo(print())
                .andExpect(status().reason("Input for Minutes amount is less than 0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void spendGigabytesFromExistingAccPositive() throws Exception {
        this.mockMvc.perform(post("/api/accounts/gigabytes/spend/{phoneNumber}/{gigabytesAmount}", "+79213335671", "7"))
                .andDo(print())
                .andExpect(content().string("3"))
                .andExpect(status().isOk());
    }

    @Test
    public void spendGigabytesFromInactiveAccNegative() throws Exception {
        this.mockMvc.perform(post("/api/accounts/gigabytes/spend/{phoneNumber}/{minutesAmount}", "+79213335673", "5"))
                .andDo(print())
                .andExpect(status().reason("Account with phone +79213335673 is inactive"))
                .andExpect(status().isBadRequest());
    }
}
