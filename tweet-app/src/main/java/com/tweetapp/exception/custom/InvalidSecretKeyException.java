package com.tweetapp.exception.custom;

public class InvalidSecretKeyException extends RuntimeException {

    public InvalidSecretKeyException(String message){
        super(message);
    }
}
