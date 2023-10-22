package ru.apmgor.analyticservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.apmgor.analyticservice.entity.AnalyticProduct;

public interface AnalyticRepository extends ReactiveCrudRepository<AnalyticProduct, Integer> {

    @Query("SELECT * FROM product_views ORDER BY count DESC LIMIT 3")
    Flux<AnalyticProduct> findTop3ByCount();
}
