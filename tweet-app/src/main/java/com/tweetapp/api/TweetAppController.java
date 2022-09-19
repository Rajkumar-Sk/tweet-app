package com.tweetapp.api;

import com.tweetapp.domain.Reply;
import com.tweetapp.domain.Tweet;
import com.tweetapp.service.TweetAppServiceImpl;
import com.tweetapp.dto.ReplyDto;
import com.tweetapp.dto.TweetDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetAppController {
    private final TweetAppServiceImpl tweetAppServiceImpl;

    public TweetAppController(TweetAppServiceImpl tweetAppServiceImpl) {
        this.tweetAppServiceImpl = tweetAppServiceImpl;
    }

    /**
     * Get all tweets posted by users
     *
     * @return
     */
    @ApiOperation(value = "GET_ALL_TWEETS",
                  produces = "application/json",
                  authorizations = {@Authorization(value = "token")})
    @GetMapping(value = "/all",
                produces = {MediaType.APPLICATION_JSON_VALUE},
                name = "GET_ALL_TWEETS")
    public ResponseEntity<List<Tweet>> getAllTweets() {
        return new ResponseEntity<>(tweetAppServiceImpl.getAllTweets(), HttpStatus.OK);
    }


    /**
     * Get all tweets of a user
     *
     * @param username
     * @return
     */
    @ApiOperation(value = "GET_ALL_TWEETS_OF_USER",
                  produces = "application/json",
                  authorizations = {@Authorization(value = "token")})
    @GetMapping(value = "/{username}",
                produces = {MediaType.APPLICATION_JSON_VALUE},
                name = "GET_ALL_TWEETS_OF_USER")
    public ResponseEntity<List<Tweet>> getAllTweetsOfUser(@PathVariable(value = "username") String username) {
        return new ResponseEntity<>(tweetAppServiceImpl.getAllTweetsOfUser(username), HttpStatus.OK);
    }

    /**
     * Post new tweet of a user
     *
     * @param username
     * @param tweet
     * @return
     */
    @ApiOperation(value = "POST_NEW_TWEET",
                  consumes = "application/json",
                  produces = "application/json",
                  authorizations = {@Authorization(value = "token")})
    @PostMapping(value = "/{username}/add",
                  consumes = {MediaType.APPLICATION_JSON_VALUE},
                  produces = {MediaType.APPLICATION_JSON_VALUE},
                  name = "POST_NEW_TWEET")
    public ResponseEntity<Tweet> postNewTweet(@PathVariable(value = "username") String username,
                                              @RequestBody @Valid Tweet tweet) {
        return new ResponseEntity<>(tweetAppServiceImpl.postNewTweet(username, tweet), HttpStatus.CREATED);
    }

    /**
     * Update tweet of a user
     *
     * @param username
     * @param tweetId
     * @return
     */
    @ApiOperation(value = "UPDATE_TWEET",
                  consumes = "application/json",
                  authorizations = {@Authorization(value = "token")})
    @PutMapping(value = "/{username}/update/{id}",
                consumes = {MediaType.APPLICATION_JSON_VALUE},
                name = "UPDATE_TWEET")
    public ResponseEntity<Tweet> updateTweet(@PathVariable(value = "username") String username,
                                             @PathVariable(value = "id") String tweetId,
                                             @RequestBody @Valid TweetDto tweetDto) {
        return new ResponseEntity<>(tweetAppServiceImpl.updateTweet(username, tweetId, tweetDto), HttpStatus.CREATED);

    }

    /**
     * Delete tweet of a user
     *
     * @param username
     * @param tweetId
     * @return
     */
    @ApiOperation(value = "DELETE_TWEET",
                  authorizations = {@Authorization(value = "token")})
    @DeleteMapping(value = "/{username}/delete/{id}",
                   name = "DELETE_TWEET")
    public ResponseEntity<String> deleteTweet(@PathVariable(value = "username") String username,
                                              @PathVariable(value = "id") String tweetId) {
        tweetAppServiceImpl.deleteTweet(username, tweetId);
        return new ResponseEntity<>(tweetId + " has been deleted", HttpStatus.OK);

    }

    /**
     * Add like to tweet
     *
     * @param username
     * @param tweetId
     * @return
     */
    @ApiOperation(value = "LIKE_TWEET",
                  produces = "application/json",
                  authorizations = {@Authorization(value = "token")})
    @PutMapping(value = "/{username}/like/{id}",
                produces = {MediaType.APPLICATION_JSON_VALUE},
                name = "LIKE_TWEET")
    public ResponseEntity<Tweet> addLike(@PathVariable(value = "username") String username,
                                         @PathVariable(value = "id") String tweetId) {
        return new ResponseEntity<>(tweetAppServiceImpl.addLike(username, tweetId), HttpStatus.OK);

    }

    /**
     * Reply to tweet
     *
     * @param username
     * @param tweetId
     * @param replyDto
     * @return
     */
    @ApiOperation(value = "REPLY_TO_TWEET",
            consumes = "application/json",
            produces = "application/json",
            authorizations = {@Authorization(value = "token")})
    @PostMapping(value = "/{username}/reply/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            name = "REPLY_TO_TWEET")
    public ResponseEntity<Reply> addReply(@PathVariable(value = "username") String username,
                                          @PathVariable(value = "id") String tweetId,
                                          @RequestBody @Valid ReplyDto replyDto) {
        Reply reply = new Reply(replyDto.getTweetReply(),replyDto.getReplyTag());
        return new ResponseEntity<>(tweetAppServiceImpl.replyToTweet(username, tweetId, reply), HttpStatus.CREATED);
    }


}
