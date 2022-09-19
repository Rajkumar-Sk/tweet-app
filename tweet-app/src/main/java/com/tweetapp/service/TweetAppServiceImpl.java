package com.tweetapp.service;

import com.tweetapp.domain.Reply;
import com.tweetapp.domain.Tweet;
import com.tweetapp.domain.TweetAppUser;
import com.tweetapp.dto.TweetDto;
import com.tweetapp.exception.ExceptionMessages;
import com.tweetapp.exception.custom.EmptyTweetException;
import com.tweetapp.exception.custom.TweetNotFoundException;
import com.tweetapp.exception.custom.UserNotFoundException;
import com.tweetapp.repository.TweetAppUserRepository;
import com.tweetapp.repository.TweetReplyRepository;
import com.tweetapp.repository.TweetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class TweetAppServiceImpl implements TweetAppService {

    private static final Logger LOG = LoggerFactory.getLogger(TweetAppServiceImpl.class);

    @Autowired
    private TweetAppUserRepository tweetAppUserRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TweetReplyRepository tweetReplyRepository;

//    @Autowired
//    private KafkaService kafkaService;

    @Override
    public List<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }

    @Override
    public List<Tweet> getAllTweetsOfUser(String username) {
        Optional<TweetAppUser> optionalUser = tweetAppUserRepository.findByLoginId(username);
        if (!optionalUser.isPresent()) {
            LOG.info("USER NOT FOUND");
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }
        return tweetRepository.findAllTweetsByLoginId(username);
    }

    public Tweet postNewTweet(String username, Tweet tweet) {
        tweet.setLoginId(username);
        tweet.setPostedTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        tweetRepository.save(tweet);
     //   kafkaService.sendMessage("TWEET HAS BEEN ADDED BY USER " + username);
        return tweet;
    }

    @Override
    public Tweet updateTweet(String username, String tweetId, TweetDto tweetDto) {
        if (tweetDto.getTweet() == null || tweetDto.getTweet().isEmpty()) {
            LOG.info("TWEET IS EMPTY FROM TWEET VO FOR TWEET ID {}", tweetId);
            throw new EmptyTweetException(ExceptionMessages.TWEET_IS_EMPTY);
        }
        Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
        if (!optionalTweet.isPresent()) {
            LOG.info("TWEET NOT FOUND {}", tweetId);
            throw new TweetNotFoundException(ExceptionMessages.TWEET_NOT_FOUND);
        }
        Tweet existingTweet = optionalTweet.get();
        existingTweet.setUserTweet(tweetDto.getTweet());
        existingTweet.setPostedTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        tweetRepository.save(existingTweet);
//        kafkaService.sendMessage("TWEET HAS BEEN UPDATED BY USER " + username);
        return existingTweet;
    }

    @Override
    public void deleteTweet(String username, String tweetId) {
        Optional<TweetAppUser> optionalUser = tweetAppUserRepository.findByLoginId(username);
        if (!optionalUser.isPresent()) {
            LOG.info("USER NOT FOUND FOR USERNAME {}", username);
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }
        Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
        if (!optionalTweet.isPresent()) {
            LOG.info("TWEET NOT FOUND FOR TWEET ID {}", tweetId);
            throw new TweetNotFoundException(ExceptionMessages.TWEET_NOT_FOUND);
        }
        tweetRepository.delete(optionalTweet.get());
//        kafkaService.sendMessage("TWEET HAS BEEN DELETED BY USER " + username);
    }

    @Override
    public Tweet addLike(String username, String tweetId) {
        Optional<TweetAppUser> optionalUser = tweetAppUserRepository.findByLoginId(username);
        if (!optionalUser.isPresent()) {
            LOG.info("USER NOT FOUND {}", username);
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }

        Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
        if (!optionalTweet.isPresent()) {
            LOG.info("TWEET NOT FOUND - TWEET ID {}", tweetId);
            throw new TweetNotFoundException(ExceptionMessages.TWEET_NOT_FOUND);
        }

        Tweet tweet = optionalTweet.get();
        if (tweet.getLikedBY() != null && !tweet.getLikedBY().isEmpty()) {
            if (tweet.getLikedBY().contains(username)) {
                tweet.setLike(tweet.getLike() - 1);
                tweet.getLikedBY().remove(username);
            } else {
                tweet.getLikedBY().add(username);
                tweet.setLike(tweet.getLike() + 1);
            }
        } else {
            tweet.setLikedBY(Arrays.asList(username));
            tweet.setLike(tweet.getLike() + 1);
        }
        tweetRepository.save(tweet);
        return tweet;
    }

    @Override
    public Reply replyToTweet(String username, String tweetId, Reply reply) {
        Optional<TweetAppUser> optionalUser = tweetAppUserRepository.findByLoginId(username);
        if (!optionalUser.isPresent()) {
            LOG.info("USER IS NOT FOUND FOR USERNAME {}", username);
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }
        reply.setRepliedBy(username);
        reply.setRepliedOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Reply persisted = tweetReplyRepository.save(reply);
        Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
        if (!optionalTweet.isPresent()) {
            LOG.info("TWEET IS NOT FOUND {}", tweetId);
            throw new TweetNotFoundException(ExceptionMessages.TWEET_NOT_FOUND);
        }

        Tweet tweet = optionalTweet.get();
        if (tweet.getReplies() != null && !tweet.getReplies().isEmpty())
            tweet.getReplies().add(persisted);
        else
            tweet.setReplies(Arrays.asList(persisted));
        tweetRepository.save(tweet);
        return reply;
    }

}


