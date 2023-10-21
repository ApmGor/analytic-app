package ru.apmgor.analyticservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.apmgor.analyticservice.entity.AnalyticProduct;

public interface AnalyticRepository extends ReactiveCrudRepository<AnalyticProduct, Integer> {

    @Query("INSERT INTO product_views (product_id, count) VALUES ($1, $2)")
    Mono<AnalyticProduct> insertProduct(final Integer id, final Integer count);
}
