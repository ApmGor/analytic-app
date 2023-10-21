package ru.apmgor.productservice.kafka;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;
import ru.apmgor.common.event.ProductViewEvent;

@Configuration
public class KafkaConfig {

    @Bean
    public SenderOptions<String, ProductViewEvent> senderOptions(final KafkaProperties properties) {
        return SenderOptions.create(properties.buildProducerProperties());
    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, ProductViewEvent> producerTemplate(
            final SenderOptions<String, ProductViewEvent> options
    ) {
        return new ReactiveKafkaProducerTemplate<>(options);
    }
}
