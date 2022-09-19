//package com.tweetapp.configuration.kafka;
//
//import org.apache.kafka.clients.admin.AdminClientConfig;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.KafkaAdmin;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaTopicConfig {
//
//    @Value("${kafka.bootstrap.servers.address}")
//    private String bootstrapServersAddress;
//
//    @Value("${kafka.topic.name}")
//    private String kafkaTopicName;
//
//    @Value("${kafka.logs.topic.name}")
//    private String kafkaLogsTopicName;
//
//    @Value("${kafka.partitions.number}")
//    private int kafkaPartitionsNumber;
//
//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersAddress);
//        return new KafkaAdmin(configs);
//    }
//
//    @Bean
//    public NewTopic topic() {
//        return new NewTopic(kafkaTopicName, kafkaPartitionsNumber, (short) 1);
//    }
//
//    @Bean
//    public NewTopic topic1() {
//        return new NewTopic(kafkaLogsTopicName, kafkaPartitionsNumber, (short) 1);
//    }
//}
