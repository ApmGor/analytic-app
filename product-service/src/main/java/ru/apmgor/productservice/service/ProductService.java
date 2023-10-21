package ru.apmgor.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.apmgor.productservice.dto.ProductDto;
import ru.apmgor.productservice.kafka.ProductProducer;
import ru.apmgor.productservice.mapper.ProductMapper;
import ru.apmgor.productservice.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public final class ProductService {

    private final ProductRepository repository;
    public final ProductProducer producer;

    public Mono<ProductDto> getOneProduct(final Integer id) {
        return repository.findById(id)
                .flatMap(product -> producer
                        .sendEvent(ProductMapper.toEvent(product))
                        .thenReturn(product))
                .map(ProductMapper::toDto);
    }
}
