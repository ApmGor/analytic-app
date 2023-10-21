package ru.apmgor.productservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;
import ru.apmgor.common.event.ProductViewEvent;

@Component
@RequiredArgsConstructor
public final class ProductProducer {

    private final ReactiveKafkaProducerTemplate<String, ProductViewEvent> template;

    public Mono<SenderResult<Void>> sendEvent(final ProductViewEvent event) {
        return template.send("products-analytic", event.productId().toString(), event);
    }
}
