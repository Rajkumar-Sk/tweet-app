package com.tweetapp.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
public class Reply {

    @Id
    private String id;
    private String repliedBy;
    private String userReply;
    private String tag;
    private String repliedOn;

    public Reply() {
    }

    public Reply(String userReply, String tag) {
        this.userReply = userReply;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepliedBy() {
        return repliedBy;
    }

    public void setRepliedBy(String repliedBy) {
        this.repliedBy = repliedBy;
    }

    public String getUserReply() {
        return userReply;
    }

    public void setUserReply(String userReply) {
        this.userReply = userReply;
    }

    public String getRepliedOn() {
        return repliedOn;
    }

    public void setRepliedOn(String repliedOn) {
        this.repliedOn = repliedOn;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

