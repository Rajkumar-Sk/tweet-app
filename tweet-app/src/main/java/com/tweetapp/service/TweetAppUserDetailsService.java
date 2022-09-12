package com.tweetapp.service;

import com.tweetapp.domain.TweetAppUser;
import com.tweetapp.exception.ExceptionMessages;
import com.tweetapp.repository.TweetAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TweetAppUserDetailsService implements UserDetailsService {

    @Autowired
    private TweetAppUserRepository tweetAppUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TweetAppUser tweetAppUser = tweetAppUserRepository.findByLoginId(username).orElseThrow(() -> new UsernameNotFoundException(ExceptionMessages.USER_NOT_FOUND));
        return new User(tweetAppUser.getLoginId(),tweetAppUser.getPassword(), new ArrayList<>());
    }
}
