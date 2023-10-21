package ru.apmgor.analyticservice.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import ru.apmgor.analyticservice.repository.AnalyticRepository;
import ru.apmgor.common.event.ProductViewEvent;

@Component
@RequiredArgsConstructor
public final class AnalyticConsumer {

    private final ReactiveKafkaConsumerTemplate<String, ProductViewEvent> template;
    private final AnalyticRepository repository;

    @PostConstruct
    private void consumeEvents() {
        template.receive()
                .concatMap(r ->
                        repository.findById(r.value().productId())
                                .map(p -> p.withCount(p.getCount() + 1))
                                .flatMap(repository::save)
                                .switchIfEmpty(repository.insertProduct(r.value().productId(), 1))
                )
                .subscribe();
    }
}
