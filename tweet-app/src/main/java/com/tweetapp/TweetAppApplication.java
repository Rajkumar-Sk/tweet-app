package com.tweetapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories("com.tweetapp.repository")
@ComponentScan(basePackages = {"com.tweetapp.service", "com.tweetapp.api", "com.tweetapp.domain",
        "com.tweetapp.configuration", "com.tweetapp.security",
        "com.tweetapp.exception.handler",
        "com.tweetapp.exception", "com.tweetapp.dto", "com.tweetapp.repository"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TweetAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TweetAppApplication.class, args);
    }

}
