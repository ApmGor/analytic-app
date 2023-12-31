package ru.apmgor.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.apmgor.productservice.dto.ProductDto;
import ru.apmgor.productservice.kafka.ProductViewEventProducer;
import ru.apmgor.productservice.mapper.ProductMapper;
import ru.apmgor.productservice.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public final class ProductService {

    private final ProductRepository repository;
    private final ProductViewEventProducer producer;

    public Mono<ProductDto> getOneProduct(final Integer id) {
        return repository.findById(id)
                .doOnNext(product -> producer.emit(ProductMapper.toEvent(product)))
                .map(ProductMapper::toDto);
    }
}
