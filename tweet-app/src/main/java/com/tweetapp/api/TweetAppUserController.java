package com.tweetapp.api;

import com.tweetapp.domain.TweetAppUser;
import com.tweetapp.service.TweetAppUserServiceImpl;
import com.tweetapp.dto.ForgotPassword;
import com.tweetapp.dto.LoginRequest;
import com.tweetapp.dto.LoginResponse;
import com.tweetapp.dto.TweetAppUserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin(origins ="http://localhost:3000")
@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetAppUserController {

    private final TweetAppUserServiceImpl tweetAppUserServiceImpl;

    public TweetAppUserController(TweetAppUserServiceImpl tweetAppUserServiceImpl) {
        this.tweetAppUserServiceImpl = tweetAppUserServiceImpl;
    }

    /**
     * Register New User to the Tweet App User Database
     *
     * @param tweetAppUserDto
     * @return
     */
    @ApiOperation(value = "REGISTER_NEW_USER",
                  consumes = "application/json",
                  produces = "application/json")
    @PostMapping(value = "/register",
                 consumes = {MediaType.APPLICATION_JSON_VALUE},
                 produces = {MediaType.APPLICATION_JSON_VALUE},
                 name = "REGISTER_NEW_USER")
    public ResponseEntity<TweetAppUser> registerNewUser(@RequestBody @Valid TweetAppUserDto tweetAppUserDto) {
        TweetAppUser tweetAppUser = tweetAppUserServiceImpl.convertDto(tweetAppUserDto);
        TweetAppUser userRegistered = tweetAppUserServiceImpl.registerNewUser(tweetAppUser);
        return new ResponseEntity<>(userRegistered, HttpStatus.CREATED);
    }

    /**
     * Authenticate User and return JWT token
     *
     * @param loginRequest
     * @return
     */
    @ApiOperation(value = "LOGIN_USER", consumes = "application/json")
    @PostMapping(value = "/login",
                 consumes = {MediaType.APPLICATION_JSON_VALUE},
                 name = "LOGIN_USER")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(tweetAppUserServiceImpl.loginUser(loginRequest), HttpStatus.OK);
    }

    /**
     * Get all users in TweetApp
     *
     * @return
     */
    @ApiOperation(value = "GET_ALL_USERS",
                  produces = "application/json",
                  authorizations = {@Authorization(value = "token")})
    @GetMapping(value = "/users/all",
                produces = {MediaType.APPLICATION_JSON_VALUE},
                name = "GET_ALL_USERS")
    public ResponseEntity<List<TweetAppUser>> getAllUsers() {
        return new ResponseEntity<>(tweetAppUserServiceImpl.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Validate User's Secret Key and enable User to set New Password
     *
     * @param username
     * @param forgotPassword
     * @return
     */
    @ApiOperation(value = "FORGOT_PASSWORD")
    @PostMapping(value = "/{username}/forgot")
    public ResponseEntity<String> forgotPassword(@PathVariable(value = "username") String username,
                                                 @RequestBody ForgotPassword forgotPassword) {
        return new ResponseEntity<>(tweetAppUserServiceImpl.forgotPassword(username, forgotPassword), HttpStatus.OK);
    }

    /**
     * List all Users by Username
     *
     * @param username
     * @return
     */
    @ApiOperation(value = "SEARCH_BY_USERNAME",
                  produces = "application/json",
                  authorizations = {@Authorization(value = "token")})
    @GetMapping(value = "/user/search/{username}",
                produces = {MediaType.APPLICATION_JSON_VALUE},
                name = "SEARCH_BY_USERNAME")
    public ResponseEntity<List<TweetAppUser>> searchByUsername(@PathVariable(value = "username") String username) {
        return new ResponseEntity<>(tweetAppUserServiceImpl.searchByUsername(username), HttpStatus.OK);
    }
}
