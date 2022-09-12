package com.tweetapp.service;

import com.tweetapp.domain.Reply;
import com.tweetapp.domain.Tweet;
import com.tweetapp.domain.TweetAppUser;
import com.tweetapp.exception.ExceptionMessages;
import com.tweetapp.exception.custom.EmptyTweetException;
import com.tweetapp.exception.custom.TweetNotFoundException;
import com.tweetapp.exception.custom.UserNotFoundException;
import com.tweetapp.repository.TweetAppUserRepository;
import com.tweetapp.repository.TweetReplyRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.dto.TweetDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestTweetAppServiceImpl {

    @InjectMocks
    private TweetAppServiceImpl tweetAppService;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private TweetAppUserRepository tweetAppUserRepository;

    @Mock
    private TweetReplyRepository tweetReplyRepository;

    @Mock
    private KafkaService kafkaService;

    private TweetAppUser tweetAppUser;
    private Tweet tweet;
    private Reply reply;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        tweetAppUser = new TweetAppUser();
        tweetAppUser.setId("id");
        tweetAppUser.setFirstName("Rajkumar");
        tweetAppUser.setLastName("S");
        tweetAppUser.setLoginId("raj");
        tweetAppUser.setEmail("raj@gmail.com");
        tweetAppUser.setPassword("rrr");
        tweetAppUser.setSecretKey("tom");

        reply = new Reply();
        reply.setId("replyId1");
        reply.setRepliedBy("akash");
        reply.setUserReply("Hi");
        reply.setRepliedOn(LocalDateTime.now().plusMinutes(2).toString());

        tweet = new Tweet();
        tweet.setLoginId("raj");
        tweet.setId("tweetId1");
        tweet.setUserTweet("Hello");
        tweet.setPostedTime(LocalDateTime.now().toString());
        tweet.setReplies(new LinkedList<>(Arrays.asList(reply)));
        tweet.setLike(2);
        tweet.setLikedBY(new LinkedList<>());

    }


    @DisplayName("Test Get All Tweets")
    @Test
    void testGetAllTweets() {
        List<Tweet> expectedList = new ArrayList<>();
        expectedList.add(tweet);
        when(tweetRepository.findAll()).thenReturn(expectedList);
        List<Tweet> actualList = tweetAppService.getAllTweets();
        assertEquals(expectedList, actualList);
    }

    @DisplayName("Test Get All Tweets of a User")
    @Test
    void testGetAllTweetsOfUser() {
        List<Tweet> expectedList = new ArrayList<>();
        expectedList.add(tweet);
        when(tweetAppUserRepository.findByLoginId("raj")).thenReturn(Optional.of(tweetAppUser));
        when(tweetRepository.findAllTweetsByLoginId("raj")).thenReturn(expectedList);
        List<Tweet> tweetList = tweetAppService.getAllTweetsOfUser("raj");
        assertEquals(expectedList, tweetList);
    }

    @DisplayName("Test Get All Tweets of a User - User Not Found Exception")
    @Test
    void testGetAllTweetsOfUserException() {
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> {
                    tweetAppService.getAllTweetsOfUser("ak");
                },
                "User not found should have thrown User Not Found Exception");
        assertEquals(ExceptionMessages.USER_NOT_FOUND, userNotFoundException.getMessage());
    }

    @DisplayName("Test Post New Tweet")
    @Test
    void testPostNewTweet() {
        when(tweetRepository.save(tweet)).thenReturn(tweet);
        Tweet actualTweet = tweetAppService.postNewTweet("raj", tweet);
        assertEquals(tweet, actualTweet);
    }

    @DisplayName("Test Update Tweet")
    @Test
    void testUpdateTweet() {
        TweetDto tweetDto = new TweetDto("Hello World");
        tweet.setUserTweet(tweetDto.getTweet());
        when(tweetRepository.findById("tweetId1")).thenReturn(Optional.of(tweet));
        when(tweetRepository.save(tweet)).thenReturn(tweet);
        Tweet actualTweet = tweetAppService.updateTweet("raj", "tweetId1", tweetDto);
        assertEquals("Hello World", actualTweet.getUserTweet());
    }

    @DisplayName("Test Update Tweet - Empty Tweet Exception")
    @Test
    void testUpdateTweetEmptyTweetException() {
     assertThrows(EmptyTweetException.class,
                () -> {
                    tweetAppService.updateTweet("raj", "tweetId1", new TweetDto(null));
                },
                "No Tweet present in Tweet VO should have thrown Empty Tweet Exception");
    }

    @DisplayName("Test Update Tweet - Tweet not found Exception")
    @Test
    void testUpdateTweetException() {
        when(tweetRepository.findById("tweetId2")).thenReturn(Optional.empty());
        assertThrows(TweetNotFoundException.class,
                () -> {
                    tweetAppService.updateTweet("raj", "tweetId2", new TweetDto("Hi"));
                },
                "Tweet not found should have thrown Tweet Not Found Exception");
    }

    @DisplayName("Test Delete Tweet")
    @Test
    void testDeleteTweet() {
        when(tweetAppUserRepository.findByLoginId("raj")).thenReturn(Optional.of(tweetAppUser));
        when(tweetRepository.findById("tweetId1")).thenReturn(Optional.of(tweet));
        tweetAppService.deleteTweet("raj", "tweetId1");
        verify(tweetRepository, times(1)).delete(tweet);
    }

    @DisplayName("Test Delete Tweet - User not found Exception")
    @Test
    void testDeleteTweetUserNotFoundException() {
        assertThrows(UserNotFoundException.class,
                () -> {
                    tweetAppService.deleteTweet("ak", "tweetId2");
                },
                "User not found should have thrown User Not Found Exception");
        ;
    }

    @DisplayName("Test Delete Tweet - Tweet not found Exception")
    @Test
    void testDeleteTweetNullPointerException() {
        when(tweetAppUserRepository.findByLoginId("raj")).thenReturn(Optional.of(tweetAppUser));
        when(tweetRepository.findById("tweetId2")).thenReturn(Optional.empty());
        assertThrows(TweetNotFoundException.class,
                () -> {
                    tweetAppService.deleteTweet("raj", "tweetId2");
                });
    }

    @DisplayName("Test Add Like to Tweet by Same User")
    @Test
    void testAddLikeToTweet() {
        when(tweetAppUserRepository.findByLoginId("raj")).thenReturn(Optional.of(tweetAppUser));
        when(tweetRepository.findById("tweetId1")).thenReturn(Optional.of(tweet));
        tweetAppService.addLike("raj", "tweetId1");
        assertEquals(3,tweet.getLike());
    }

    @DisplayName("Test Add Like to Tweet by Other User")
    @Test
    void testAddLikeToTweetByOtherUser() {
        tweet.getLikedBY().add("raj");
        TweetAppUser otherUser = new TweetAppUser();
        otherUser.setLoginId("rk");
        when(tweetRepository.findById("tweetId1")).thenReturn(Optional.of(tweet));
        when(tweetAppUserRepository.findByLoginId("rk")).thenReturn(Optional.of(otherUser));
        tweetAppService.addLike("rk", "tweetId1");
        assertEquals(3,tweet.getLike());
    }

    @DisplayName("Test Dislike to Tweet")
    @Test
    void testDislikeTweetByOtherUser() {
        tweet.getLikedBY().add("raj");
        when(tweetAppUserRepository.findByLoginId("raj")).thenReturn(Optional.of(tweetAppUser));
        when(tweetRepository.findById("tweetId1")).thenReturn(Optional.of(tweet));
        tweetAppService.addLike("raj", "tweetId1");
        assertEquals(1,tweet.getLike());
    }

    @DisplayName("Test Add Like To Tweet - User not found Exception")
    @Test
    void testAddLikeToTweetUserNotFoundException() {
       assertThrows(UserNotFoundException.class,
                () -> {
                    tweetAppService.addLike("raj", "tweetId2");
                },
                "User not found should have thrown User Not Found Exception");
    }

    @DisplayName("Test Add Like To Tweet - Tweet not found Exception")
    @Test
    void testAddLikeToTweetException() {
        when(tweetAppUserRepository.findByLoginId("raj")).thenReturn(Optional.of(tweetAppUser));
        when(tweetRepository.findById("tweetId2")).thenReturn(Optional.empty());
        assertThrows(TweetNotFoundException.class,
                () -> {
                    tweetAppService.addLike("raj", "tweetId2");
                },
                "Tweet not found should have thrown Tweet Not found Exception");
    }

    @DisplayName("Test Reply to Tweet")
    @Test
    void testReplyToTweet() {
        when(tweetAppUserRepository.findByLoginId("raj")).thenReturn(Optional.of(tweetAppUser));
        when(tweetRepository.findById("tweetId1")).thenReturn(Optional.of(tweet));
        when(tweetReplyRepository.save(reply)).thenReturn(reply);
        tweetAppService.replyToTweet("raj", "tweetId1", reply);
        assertEquals(reply,tweetAppService.replyToTweet("raj", "tweetId1", reply));
        tweet.setReplies(null);
        assertEquals(reply,tweetAppService.replyToTweet("raj", "tweetId1", reply));
    }

    @DisplayName("Test Add Reply To Tweet - User not found Exception")
    @Test
    void testReplyToTweetUserNotFoundException() {
        assertThrows(UserNotFoundException.class,
                () -> {
                    tweetAppService.replyToTweet("raj", "tweetId2", reply);
                },
                "User not found should have thrown User Not Found Exception");
    }

    @DisplayName("Test Add Like To Tweet - Tweet not found Exception")
    @Test
    void testAddReplyToTweetException() {
        when(tweetAppUserRepository.findByLoginId("raj")).thenReturn(Optional.of(tweetAppUser));
        when(tweetReplyRepository.save(reply)).thenReturn(reply);
       assertThrows(TweetNotFoundException.class,
                () -> {
                    tweetAppService.replyToTweet("raj", "tweetId2", reply);
                },
                "TweetDto not found should have thrown Tweet Not found Exception");
    }

}
