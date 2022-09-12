package com.tweetapp.repository;

import com.tweetapp.domain.TweetAppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetAppUserRepository extends MongoRepository<TweetAppUser, String> {

    Optional<TweetAppUser> findByLoginId(String loginId);

    List<TweetAppUser> findByLoginIdIsLike(String loginId);

}
