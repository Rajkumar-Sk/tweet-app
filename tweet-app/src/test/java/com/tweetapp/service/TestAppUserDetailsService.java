package com.tweetapp.service;

import com.tweetapp.domain.TweetAppUser;
import com.tweetapp.repository.TweetAppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TestAppUserDetailsService {

    @InjectMocks
    private TweetAppUserDetailsService tweetAppUserDetailsService;

    @Mock
    private TweetAppUserRepository tweetAppUserRepository;

    private TweetAppUser tweetAppUser;

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
    }

    @DisplayName("Test Load User by Username")
    @Test
    void testLoadUserByUsername() {
        when(tweetAppUserRepository.findByLoginId("raj")).thenReturn(Optional.of(tweetAppUser));
        assertEquals("raj", tweetAppUserDetailsService.loadUserByUsername("raj").getUsername());
    }
}
