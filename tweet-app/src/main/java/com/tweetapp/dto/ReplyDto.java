package com.tweetapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class ReplyDto {

    @NotBlank(message = "Reply should not be empty")
    @Size(max = 144, message = "Reply should not contains more than 144 characters")
    private String tweetReply;
    @Size(max = 50, message = "Tag name should not contains more than 50 characters")
    private String replyTag;

    public String getTweetReply() {
        return tweetReply;
    }

    public void setTweetReply(String tweetReply) {
        this.tweetReply = tweetReply;
    }

    public String getReplyTag() {
        return replyTag;
    }

    public void setReplyTag(String tagDto) {
        this.replyTag = tagDto;
    }
}
