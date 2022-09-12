package com.tweetapp.exception;

public class ExceptionMessages {

    private ExceptionMessages(){
        throw new IllegalStateException("Utility class");
    }

    public static final String USER_NOT_FOUND = "User not found";
    public static final String TWEET_NOT_FOUND = "Tweet not found";
    public static final String TWEET_IS_EMPTY = "Tweet is empty";
    public static final String INVALID_USERNAME_OR_PASSWORD = "Invalid Username or Password";
    public static final String INVALID_USERNAME_OR_SECRET_KEY = "Invalid Username or Secret Key";
    public static final String INVALID_SECRET_KEY = "Secret Key does not match with existing key";
}
