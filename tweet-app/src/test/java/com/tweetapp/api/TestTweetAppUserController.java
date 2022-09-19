package com.tweetapp.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.dto.LoginRequest;
import com.tweetapp.repository.TweetAppUserRepository;
import com.tweetapp.service.TweetAppUserServiceImpl;
import com.tweetapp.dto.ForgotPassword;
import com.tweetapp.dto.TweetAppUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@WebAppConfiguration
class TestTweetAppUserController {

    @InjectMocks
    private TestTweetAppUserController testTweetAppUserController;

    @Mock
    private TweetAppUserServiceImpl tweetAppUserService;

    @Mock
    private TweetAppUserRepository tweetAppUserRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private TweetAppUserDto tweetAppUser;
    private LoginRequest loginRequest;
    private ForgotPassword forgotPassword;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        tweetAppUser = new TweetAppUserDto();
        tweetAppUser.setFirstNameDto("Rajkumar");
        tweetAppUser.setLastNameDto("S");
        tweetAppUser.setLoginIdDto("test"+new Random());
        tweetAppUser.setEmailDto("test2@gmail.com"+new Random());
        tweetAppUser.setPasswordDto("rrr");
        tweetAppUser.setConfirmPasswordDto("rrr");
        tweetAppUser.setSecretKeyDto("tom");
        tweetAppUser.setContactNumberDto("9876-543-210");

        loginRequest = new LoginRequest();
        loginRequest.setLoginId("testId");
        loginRequest.setPassword("rk");

        forgotPassword = new ForgotPassword();
        forgotPassword.setUsername("testId");
        forgotPassword.setSecretKey("tom");
        forgotPassword.setNewPassword("rk");
    }

    private String mapToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @DisplayName("Test Register New User API")
    @Test
    void testRegisterNewUserApi() throws Exception {
        String uri = "/api/v1.0/tweets/register";
        String tweetAppUserJson = mapToJson(tweetAppUser);
        MvcResult mvcResult = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(tweetAppUserJson))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
    }

    @DisplayName("Test Login API")
    @Test
    void testLoginApi() throws Exception {
        String uri = "/api/v1.0/tweets/login";
        String loginRequestVOJson = mapToJson(loginRequest);
        MvcResult mvcResult = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(loginRequestVOJson))
                .andReturn();
       // assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @DisplayName("Test Get All Users API")
    @Test
    void testGetAllUsersApi() throws Exception {
        String uri = "/api/v1.0/tweets/users/all";
        MvcResult mvcResult = mockMvc.perform(get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @DisplayName("Test Forgot Password API")
    @Test
    void testForgotPasswordApi() throws Exception {
        String uri = "/api/v1.0/tweets/{username}/forgot";
        String username = "testId";
        String forgotPwdJson = mapToJson(forgotPassword);
        MvcResult mvcResult = mockMvc.perform(get(uri, username)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(forgotPwdJson))
                .andReturn();
       // assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @DisplayName("Test Search by Username API")
    @Test
    void testSearchByUsernameApi() throws Exception {
        String uri = "/api/v1.0/tweets/user/search/{username}";
        String username = "testId";
        MvcResult mvcResult = mockMvc.perform(get(uri, username)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

}
