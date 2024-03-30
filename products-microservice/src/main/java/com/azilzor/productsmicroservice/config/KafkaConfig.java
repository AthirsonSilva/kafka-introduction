package com.azilzor.productsmicroservice.config;

import com.azilzor.productsmicroservice.kafka.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    protected NewTopic createTopic() {
        return TopicBuilder
                .name(KafkaConstants.PRODUCTS_CREATED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
