package ru.apmgor.analyticservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;
import ru.apmgor.common.event.ProductViewEvent;

import java.util.List;

@Configuration
public class ConsumerConfig {

    @Value("${app.kafka.topic.name}")
    private String topicName;

    @Bean
    public ReceiverOptions<String, ProductViewEvent> receiverOptions(final KafkaProperties properties) {
        return ReceiverOptions.<String, ProductViewEvent>create(properties.buildConsumerProperties())
                .subscription(List.of(topicName));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, ProductViewEvent> producerTemplate(
            final ReceiverOptions<String, ProductViewEvent> options
    ) {
        return new ReactiveKafkaConsumerTemplate<>(options);
    }
}
