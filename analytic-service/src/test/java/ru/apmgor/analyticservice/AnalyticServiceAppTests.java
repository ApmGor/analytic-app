package ru.apmgor.analyticservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.apmgor.analyticservice.dto.AnalyticProductDto;
import ru.apmgor.common.event.ProductViewEvent;

import java.util.List;
import java.util.stream.IntStream;

@AutoConfigureWebTestClient
public class AnalyticServiceAppTests extends AbstractKafkaTest {

    @Autowired
    private WebTestClient client;

    @Test
    void analyticTest() {

        var events = Flux.just(
                createEvent(2,2),
                createEvent(1,1),
                createEvent(6,3),
                createEvent(4,2),
                createEvent(5,5),
                createEvent(4,2),
                createEvent(6,3),
                createEvent(3,3)
        ).flatMap(Flux::fromIterable)
                .map(event -> toSenderRecord(KAFKA_TOPIC, event.productId().toString(), event));

        var result = this.<ProductViewEvent>createSender().send(events);

        StepVerifier.create(result)
                .expectNextCount(21)
                .verifyComplete();

        var mono = client.get()
                .uri("/analytics")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .returnResult(new ParameterizedTypeReference<List<AnalyticProductDto>>() {})
                .getResponseBody()
                .next();

        StepVerifier.create(mono)
                .consumeNextWith(this::validate)
                .verifyComplete();

    }

    private void validate(final List<AnalyticProductDto> list) {
        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals(6, list.get(0).productId());
        Assertions.assertEquals(6, list.get(0).count());
        Assertions.assertEquals(4, list.get(2).productId());
        Assertions.assertEquals(4, list.get(2).count());
        Assertions.assertTrue(list.stream().noneMatch(dto -> dto.productId() == 1));
    }

    private List<ProductViewEvent> createEvent(final int productId, final int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> new ProductViewEvent(productId))
                .toList();
    }
}
