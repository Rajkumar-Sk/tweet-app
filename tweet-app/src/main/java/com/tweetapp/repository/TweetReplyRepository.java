package com.tweetapp.repository;

import com.tweetapp.domain.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetReplyRepository extends MongoRepository<Reply, String> {

}
