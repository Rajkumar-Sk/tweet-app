package com.tweetapp.service;

import com.tweetapp.domain.Reply;
import com.tweetapp.domain.Tweet;
import com.tweetapp.dto.TweetDto;

import java.util.List;

public interface TweetAppService {

    List<Tweet> getAllTweets();

    List<Tweet> getAllTweetsOfUser(String username);

    Tweet postNewTweet(String username, Tweet tweet);

    Tweet updateTweet(String username, String tweetId, TweetDto tweetDto);

    void deleteTweet(String username, String tweetId);

    Tweet addLike(String username, String tweetId);

    Reply replyToTweet(String username, String tweetId, Reply reply);

}
