package ru.apmgor.productservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.apmgor.productservice.entity.Product;

public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
}
