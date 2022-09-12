package com.tweetapp.service;

import com.tweetapp.domain.TweetAppUser;
import com.tweetapp.dto.ForgotPassword;
import com.tweetapp.dto.LoginRequest;
import com.tweetapp.dto.LoginResponse;

import java.util.List;

public interface TweetAppUserService {

    TweetAppUser registerNewUser(TweetAppUser tweetAppUser);

    LoginResponse loginUser(LoginRequest loginRequest);

    String forgotPassword(String username, ForgotPassword forgotPassword);

    List<TweetAppUser> getAllUsers();

    List<TweetAppUser> searchByUsername(String username);
}
