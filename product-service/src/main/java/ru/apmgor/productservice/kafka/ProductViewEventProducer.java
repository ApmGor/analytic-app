package ru.apmgor.productservice.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.kafka.sender.SenderRecord;
import ru.apmgor.common.event.ProductViewEvent;

@Slf4j
@RequiredArgsConstructor
public final class ProductViewEventProducer {

    private final ReactiveKafkaProducerTemplate<String, ProductViewEvent> template;
    private final Sinks.Many<ProductViewEvent> sink;
    private final Flux<ProductViewEvent> flux;
    private final String topic;

    @PostConstruct
    public void produceEvent() {
        val tmpFlux = flux
                .map(pwe -> new ProducerRecord<>(topic, pwe.productId().toString(), pwe))
                .map(rec -> SenderRecord.create(rec, rec.key()));
        template.send(tmpFlux)
                .doOnNext(res -> log.info("Emitted event: {}", res.correlationMetadata()))
                .subscribe();
    }

    public void emit(final ProductViewEvent event) {
        sink.tryEmitNext(event);
    }
}
