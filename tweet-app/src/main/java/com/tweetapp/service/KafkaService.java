package com.tweetapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String kafkaTopicName;

    private static final Logger LOG = LoggerFactory.getLogger(KafkaService.class);

    public void sendMessage(String message) {
        LOG.info("NEW MESSAGE HAS BEEN SENT TO KAFKA");
        kafkaTemplate.send(kafkaTopicName, message);
    }

    @KafkaListener(topics = "tweet-app", groupId = "tweet-app-group")
    public void consumerMsg(String message) {
        LOG.info("NEW MESSAGE HAS BEEN READ FROM KAFKA \"{}\"", message);
    }
}
