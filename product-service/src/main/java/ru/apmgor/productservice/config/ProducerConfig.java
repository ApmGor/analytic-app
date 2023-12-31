package ru.apmgor.productservice.config;

import lombok.val;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Sinks;
import reactor.kafka.sender.SenderOptions;
import ru.apmgor.common.event.ProductViewEvent;
import ru.apmgor.productservice.kafka.ProductViewEventProducer;

@Configuration
public class ProducerConfig {

    @Value("${app.kafka.topic.name}")
    private String topicName;

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

    @Bean
    public ProductViewEventProducer productViewEventProducer(
            final ReactiveKafkaProducerTemplate<String, ProductViewEvent> template
    ) {
        val sink = Sinks.many().unicast().<ProductViewEvent>onBackpressureBuffer();
        val flux = sink.asFlux();
        return new ProductViewEventProducer(template, sink, flux, topicName);
    }

    @Bean
    public NewTopic createTopic() {
        return TopicBuilder.name(topicName)
                .partitions(3)
                .replicas(3)
                .build();
    }
}
