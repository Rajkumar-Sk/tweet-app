package com.tweetapp.service;

import com.tweetapp.domain.TweetAppUser;
import com.tweetapp.dto.LoginRequest;
import com.tweetapp.exception.ExceptionMessages;
import com.tweetapp.exception.custom.InvalidSecretKeyException;
import com.tweetapp.exception.custom.UserNotFoundException;
import com.tweetapp.repository.TweetAppUserRepository;
import com.tweetapp.security.utils.JwtTokenUtils;
import com.tweetapp.dto.ForgotPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestTweetAppUserServiceImpl {

    @InjectMocks
    private TweetAppUserServiceImpl tweetAppUserServiceImpl;

    @Mock
    private TweetAppUserRepository tweetAppUserRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private TweetAppUserDetailsService tweetAppUserDetailsService;

//    @Mock
//    private KafkaService kafkaService;

    private TweetAppUser tweetAppUser;
    private UserDetails userDetails;
    private ForgotPassword forgotPassword;
    private LoginRequest loginRequest;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);

        tweetAppUser = new TweetAppUser();
        tweetAppUser.setId("id");
        tweetAppUser.setFirstName("Rajkumar");
        tweetAppUser.setLastName("S");
        tweetAppUser.setLoginId("raj");
        tweetAppUser.setEmail("raj@gmail.com");
        tweetAppUser.setPassword("rrr");
        tweetAppUser.setSecretKey("tom");

        userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return "raj";
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        };

        forgotPassword = new ForgotPassword();
        forgotPassword.setUsername("raj");
        forgotPassword.setNewPassword("rk");
        forgotPassword.setSecretKey("tom");

        loginRequest = new LoginRequest();
        loginRequest.setLoginId("raj");
        loginRequest.setPassword("rrr");

    }

    @DisplayName("Test Register New User")
    @Test
    void testRegisterNewUser() {
        when(tweetAppUserRepository.save(tweetAppUser)).thenReturn(tweetAppUser);
        TweetAppUser tweetAppUser = tweetAppUserServiceImpl.registerNewUser(this.tweetAppUser);
        assertEquals("raj", tweetAppUser.getLoginId());
    }

    @DisplayName("Test LoginRequest")
    @Test
    void testLogin() {
        when(tweetAppUserDetailsService.loadUserByUsername("raj")).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn("token");
        assertEquals("token", tweetAppUserServiceImpl.loginUser(loginRequest).getToken());

    }

    @DisplayName("Test LoginRequest - Bad Credential Exception")
    @Test
    void testLoginException() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId("raj");
        loginRequest.setPassword("rr");
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
        BadCredentialsException badCredentialsException = assertThrows(BadCredentialsException.class,
                () -> {
                    tweetAppUserServiceImpl.loginUser(loginRequest);
                },
                "Invalid Username or Password");
        assertEquals(ExceptionMessages.INVALID_USERNAME_OR_PASSWORD, badCredentialsException.getMessage());

    }

    @DisplayName("Test Forgot Password")
    @Test
    void testGetForgotPassword() {
        when(tweetAppUserRepository.findByLoginId("raj")).thenReturn(Optional.of(tweetAppUser));
        assertEquals("Password has been changed", tweetAppUserServiceImpl.forgotPassword("raj", forgotPassword));
    }

    @DisplayName("Test Forgot Password - Invalid Username or Secret Key Exception")
    @Test
    void testForgotPasswordException() {
        forgotPassword.setUsername("");
        forgotPassword.setNewPassword("");
        forgotPassword.setSecretKey("");
        assertThrows(NullPointerException.class,
                () -> {
                    tweetAppUserServiceImpl.forgotPassword("", forgotPassword);
                },
                "Null Username or Secret Key should have thrown User Not Found Exception");
    }

    @DisplayName("Test Forgot Password - User Not Found Exception")
    @Test
    void testForgotPasswordUserNotFoundException() {
        forgotPassword.setUsername("ak");
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> {
                    tweetAppUserServiceImpl.forgotPassword("ak", forgotPassword);
                },
                "User not found should have thrown User Not Found Exception");
        assertEquals(ExceptionMessages.USER_NOT_FOUND, userNotFoundException.getMessage());
    }

    @DisplayName("Test Forgot Password - Invalid Secret Key Exception")
    @Test
    void testForgotPasswordInvalidSecretException() {
        when(tweetAppUserRepository.findByLoginId("raj")).thenReturn(Optional.of(tweetAppUser));
        forgotPassword.setSecretKey("jack");
        InvalidSecretKeyException invalidSecretKeyException = assertThrows(InvalidSecretKeyException.class,
                () -> {
                    tweetAppUserServiceImpl.forgotPassword("raj", forgotPassword);
                },
                "Secret key does not matched should have thrown User Not Found Exception");
        assertEquals(ExceptionMessages.INVALID_SECRET_KEY, invalidSecretKeyException.getMessage());
    }

    @DisplayName("Test Get All Users")
    @Test
    void testGetAllUsers() {
        List<TweetAppUser> expectedList = new ArrayList<>();
        expectedList.add(tweetAppUser);
        when(tweetAppUserRepository.findAll()).thenReturn(expectedList);
        List<TweetAppUser> actualList = tweetAppUserServiceImpl.getAllUsers();
        assertEquals(expectedList, actualList);
    }

    @DisplayName("Test Search by Username")
    @Test
    void testSearchByUsername() {
        List<TweetAppUser> expectedList = new ArrayList<>();
        expectedList.add(tweetAppUser);
        when(tweetAppUserRepository.findByLoginIdIsLike("raj")).thenReturn(expectedList);
        List<TweetAppUser> actualList = tweetAppUserServiceImpl.searchByUsername("raj");
        assertEquals(expectedList, actualList);
    }
}
