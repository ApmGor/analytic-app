package ru.apmgor.productservice.dto;

public record ProductDto(
        Integer id,
        String description,
        Integer price
) {}
