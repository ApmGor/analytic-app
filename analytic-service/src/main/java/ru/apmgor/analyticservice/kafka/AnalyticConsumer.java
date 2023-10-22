package ru.apmgor.analyticservice.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;
import ru.apmgor.analyticservice.entity.AnalyticProduct;
import ru.apmgor.analyticservice.repository.AnalyticRepository;
import ru.apmgor.common.event.ProductViewEvent;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public final class AnalyticConsumer {

    private final ReactiveKafkaConsumerTemplate<String, ProductViewEvent> template;
    private final AnalyticRepository repository;

    @PostConstruct
    private void consumeEvents() {
        template.receive()
                .bufferTimeout(1000, Duration.ofSeconds(1))
                .flatMap(this::process)
                .subscribe();
    }

    private Mono<Void> process(final List<ReceiverRecord<String, ProductViewEvent>> events) {
        val eventsMap = getAllRecords(events);
        return repository.findAllById(eventsMap.keySet())
                .collectMap(AnalyticProduct::getProductId)
                .defaultIfEmpty(Collections.emptyMap())
                .map(dbMap -> eventsMap
                        .keySet()
                        .stream()
                        .map(productId -> updateAnalyticProduct(dbMap, eventsMap, productId)).toList())
                .flatMapMany(repository::saveAll)
                //.doOnComplete(() -> events.get(events.size() - 1).receiverOffset().acknowledge())
                .doOnError(ex -> log.error(ex.getMessage()))
                .then();
    }

    private Map<Integer, Long> getAllRecords(final List<ReceiverRecord<String, ProductViewEvent>> events) {
        return events.stream()
                .map(rec -> rec.value().productId())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private AnalyticProduct updateAnalyticProduct(
            final Map<Integer, AnalyticProduct> dbMap,
            final Map<Integer, Long> eventMap,
            final Integer productId
    ) {
        var analyticProduct = dbMap
                .getOrDefault(productId, new AnalyticProduct(productId, 0L, true));
        return analyticProduct.withCount(analyticProduct.getCount() + eventMap.get(productId));
    }
}
