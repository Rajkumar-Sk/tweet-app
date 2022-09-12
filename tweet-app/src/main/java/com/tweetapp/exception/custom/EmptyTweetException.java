package com.tweetapp.exception.custom;

public class EmptyTweetException extends RuntimeException {

    public EmptyTweetException(String message){
        super(message);
    }
}
