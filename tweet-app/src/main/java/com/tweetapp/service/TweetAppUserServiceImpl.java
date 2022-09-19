package com.tweetapp.service;

import com.tweetapp.domain.TweetAppUser;
import com.tweetapp.dto.LoginRequest;
import com.tweetapp.exception.ExceptionMessages;
import com.tweetapp.exception.custom.InvalidSecretKeyException;
import com.tweetapp.exception.custom.UserNotFoundException;
import com.tweetapp.repository.TweetAppUserRepository;
import com.tweetapp.security.utils.JwtTokenUtils;
import com.tweetapp.dto.ForgotPassword;
import com.tweetapp.dto.LoginResponse;
import com.tweetapp.dto.TweetAppUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TweetAppUserServiceImpl implements TweetAppUserService {

    private static final Logger LOG = LoggerFactory.getLogger(TweetAppUserServiceImpl.class);

    @Autowired
    private TweetAppUserRepository tweetAppUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private TweetAppUserDetailsService tweetAppUserDetailsService;

//    @Autowired
//    private KafkaService kafkaService;

    @Override
    public TweetAppUser registerNewUser(TweetAppUser tweetAppUser) {
        tweetAppUser.setPassword(new BCryptPasswordEncoder().encode(tweetAppUser.getPassword()));
        TweetAppUser persistedUser = tweetAppUserRepository.save(tweetAppUser);
       // kafkaService.sendMessage("USER " + persistedUser.getLoginId() + " HAS BEEN REGISTERED");
        return persistedUser;
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            LOG.info(ExceptionMessages.INVALID_USERNAME_OR_PASSWORD);
            throw new BadCredentialsException(ExceptionMessages.INVALID_USERNAME_OR_PASSWORD);
        }
        UserDetails userDetails = tweetAppUserDetailsService.loadUserByUsername(loginRequest.getLoginId());
        return new LoginResponse(jwtTokenUtils.generateToken(userDetails));
    }

    @Override
    public String forgotPassword(String username, ForgotPassword forgotPassword) {
        if (null == forgotPassword.getUsername() || forgotPassword.getUsername().isEmpty()
                || null == forgotPassword.getSecretKey() || forgotPassword.getSecretKey().isEmpty() || username.isEmpty()) {
            LOG.info(ExceptionMessages.INVALID_USERNAME_OR_SECRET_KEY);
            throw new NullPointerException(ExceptionMessages.INVALID_USERNAME_OR_SECRET_KEY);
        }
        Optional<TweetAppUser> optionalTweetAppUser = tweetAppUserRepository.findByLoginId(forgotPassword.getUsername());
        if (!optionalTweetAppUser.isPresent()) {
            LOG.info(ExceptionMessages.USER_NOT_FOUND);
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }
        TweetAppUser tweetAppUser = optionalTweetAppUser.get();
        if (!tweetAppUser.getSecretKey().equals(forgotPassword.getSecretKey())) {
            LOG.info(ExceptionMessages.INVALID_SECRET_KEY);
            throw new InvalidSecretKeyException(ExceptionMessages.INVALID_SECRET_KEY);
        }
        tweetAppUser.setPassword(new BCryptPasswordEncoder().encode(forgotPassword.getNewPassword()));
        tweetAppUserRepository.save(tweetAppUser);
      //  kafkaService.sendMessage("PASSWORD HAS BEEN CHANGED FOR USER " + username);
        return "Password has been changed";
    }

    @Override
    public List<TweetAppUser> getAllUsers() {
        return tweetAppUserRepository.findAll();
    }

    @Override
    public List<TweetAppUser> searchByUsername(String username) {
        return tweetAppUserRepository.findByLoginIdIsLike(username);
    }

    public TweetAppUser convertDto(TweetAppUserDto tweetAppUserDto) {
        TweetAppUser tweetAppUser = new TweetAppUser();
        tweetAppUser.setFirstName(tweetAppUserDto.getFirstNameDto());
        tweetAppUser.setLastName(tweetAppUserDto.getLastNameDto());
        tweetAppUser.setEmail(tweetAppUserDto.getEmailDto());
        tweetAppUser.setLoginId(tweetAppUserDto.getLoginIdDto());
        tweetAppUser.setPassword(tweetAppUserDto.getPasswordDto());
        tweetAppUser.setConfirmPassword(tweetAppUserDto.getConfirmPasswordDto());
        tweetAppUser.setContactNumber(tweetAppUserDto.getContactNumberDto());
        tweetAppUser.setSecretKey(tweetAppUserDto.getSecretKeyDto());
        return  tweetAppUser;
    }
}
