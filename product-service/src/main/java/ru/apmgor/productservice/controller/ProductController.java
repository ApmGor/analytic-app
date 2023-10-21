package ru.apmgor.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.apmgor.productservice.dto.ProductDto;
import ru.apmgor.productservice.service.ProductService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public final class ProductController {

    private final ProductService service;

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ProductDto>> oneProduct(@PathVariable final Integer id) {
        return service.getOneProduct(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
