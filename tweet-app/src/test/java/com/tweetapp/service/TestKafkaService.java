package com.tweetapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestKafkaService {

    @InjectMocks
    private KafkaService kafkaService;

    @Mock
    private KafkaTemplate<String,String> kafkaTemplate;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(kafkaService,"kafkaTopicName","tweet-app");
    }

    @DisplayName("Test Send Message to Kafka")
    @Test
    void testSendMessage(){

        kafkaService.sendMessage("Send Message to Kafka");
        verify(kafkaTemplate,times(1)).send("tweet-app","Send Message to Kafka");
    }

}
