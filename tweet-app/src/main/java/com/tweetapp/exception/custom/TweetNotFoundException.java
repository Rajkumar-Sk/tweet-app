package com.tweetapp.exception.custom;

public class TweetNotFoundException extends RuntimeException {

    public TweetNotFoundException(String message){
        super(message);
    }
}
