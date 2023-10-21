package ru.apmgor.productservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import ru.apmgor.common.event.ProductViewEvent;

@AutoConfigureWebTestClient
public class ProductServiceAppTests extends AbstractKafkaTest {

    @Autowired
    private WebTestClient client;

    @Test
    void productViewAndEventsTest() {

        viewProductSuccess(1);
        viewProductSuccess(1);
        viewProductError(100);
        viewProductSuccess(4);

        var flux = this.<ProductViewEvent>createReceiver(KAFKA_TOPIC)
                .receive()
                .take(3);

        StepVerifier.create(flux)
                .consumeNextWith(r -> Assertions.assertEquals(1, r.value().productId()))
                .consumeNextWith(r -> Assertions.assertEquals(1, r.value().productId()))
                .consumeNextWith(r -> Assertions.assertEquals(4, r.value().productId()))
                .verifyComplete();

    }

    private void viewProductSuccess(final int id) {
        client.get()
                .uri("/products/" + id)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id);
    }

    private void viewProductError(final int id) {
        client.get()
                .uri("/products/" + id)
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
