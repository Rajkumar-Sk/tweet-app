package com.tweetapp.repository;

import com.tweetapp.domain.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TweetReplyRepository extends MongoRepository<Reply, String> {

}
