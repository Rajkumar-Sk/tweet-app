package com.tweetapp.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.domain.Reply;
import com.tweetapp.domain.Tweet;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.service.TweetAppService;
import com.tweetapp.dto.ReplyDto;
import com.tweetapp.dto.TweetDto;
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

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@WebAppConfiguration
class TestTweetAppController {

    @InjectMocks
    private TweetAppController tweetAppController;

    private MockMvc mockMvc;

    @Mock
    private TweetAppService tweetAppService;

    @Mock
    private TweetRepository tweetRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Tweet tweet;
    private Reply reply;
    private ReplyDto replyDto;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        replyDto = new ReplyDto();
        replyDto.setTweetReply("testReply");
        reply = new Reply();
        reply.setId("replyId1");
        reply.setRepliedBy("raj");
        reply.setUserReply("testReply");
        tweet = new Tweet();
        tweet.setId("tweetId");
        tweet.setLoginId("raj");
        tweet.setUserTweet("testTweet");
        tweet.setReplies(new LinkedList<>(Arrays.asList(reply)));
        tweet.setLike(2);
        tweet.setLikedBY(new LinkedList<>());
    }

    private String mapToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }


    @DisplayName("Test Post New Tweet")
    @Test
    void testPostNewTweet() throws Exception {
        String uri = "/api/v1.0/tweets/{username}/add";
        String username = "raj";
        String tweetJson = mapToJson(tweet);
        MvcResult mvcResult = mockMvc.perform(post(uri, username)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(tweetJson))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
    }

    @DisplayName("Test Get All Tweets of user")
    @Test
    void testGetAllTweetsOfUser() throws Exception {
        String uri = "/api/v1.0/tweets/{username}";
        String username = "raj";
        MvcResult mvcResult = mockMvc.perform(get(uri, username)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        //  assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @DisplayName("Test Get All Tweets")
    @Test
    void testGetAllTweets() throws Exception {
        String uri = "/api/v1.0/tweets/all";
        MvcResult mvcResult = mockMvc.perform(get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @DisplayName("Test Update Tweet")
    @Test
    void testUpdateTweet() throws Exception {
        String uri = "/api/v1.0/tweets/{username}/update/{id}";
        String username = "raj";
        String id = "tweetId";
        TweetDto tweetDto = new TweetDto("Hi");
        String tweetVOJson = mapToJson(tweetDto);
        MvcResult mvcResult = mockMvc.perform(put(uri, username, id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(tweetVOJson))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
    }

    @DisplayName("Test Delete Tweet")
    @Test
    void testGetDeleteTweet() throws Exception {
        String uri = "/api/v1.0/tweets/{username}/delete/{id}";
        String username = "raj";
        String id = "tweetId";
        MvcResult mvcResult = mockMvc.perform(delete(uri, username, id))
                .andReturn();
//        assertEquals(200, mvcResult.getResponse().getStatus());
//        assertEquals(id + " has been deleted", mvcResult.getResponse().getContentAsString());
    }

    @DisplayName("Test Add Like")
    @Test
    void testAddLike() throws Exception {
        String uri = "/api/v1.0/tweets/{username}/like/{id}";
        String username = "raj";
        String id = "tweetId";
        String tweetjson = mapToJson(tweet);
        MvcResult mvcResult = mockMvc.perform(put(uri, username, id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(tweetjson))
                .andReturn();
//        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @DisplayName("Test Reply to Tweet")
    @Test
    void testReplyToTweet() throws Exception {
        String uri = "/api/v1.0/tweets/{username}/reply/{id}";
        String username = "raj";
        String id = "tweetId";
        String replyJson = mapToJson(replyDto);
        MvcResult mvcResult = mockMvc.perform(post(uri, username, id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(replyJson))
                .andReturn();
//        assertEquals(201, mvcResult.getResponse().getStatus());
    }


}
