package com.tweetapp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Document
public class Tweet {

    @Id
    private String id;
    private String loginId;
    private String userTweet;
    private int like;
    private List<String> likedBY;
    private List<Reply> replies;
    private String tag;
//    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private String postedTime;

    public Tweet() {
        super();
    }

    public Tweet(String userTweet, String tag) {
        this.userTweet = userTweet;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserTweet() {
        return userTweet;
    }

    public void setUserTweet(String userTweet) {
        this.userTweet = userTweet;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public List<String> getLikedBY() {
        return likedBY;
    }

    public void setLikedBY(List<String> likedBY) {
        this.likedBY = likedBY;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(String postedTime) {
        this.postedTime = postedTime;
    }

}
