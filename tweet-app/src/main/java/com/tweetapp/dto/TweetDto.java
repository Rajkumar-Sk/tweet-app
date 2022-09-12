package com.tweetapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TweetDto {

    @NotBlank(message = "Tweet should not be empty")
    @Size(max = 144, message = "Tweet should not contains more than 144 characters")
    private String tweet;

    public TweetDto() {
    }

    public TweetDto(String tweet) {
        this.tweet = tweet;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

}
