package ru.apmgor.productservice.mapper;

import ru.apmgor.productservice.dto.ProductDto;
import ru.apmgor.productservice.entity.Product;

public class ProductMapper {
    private ProductMapper() {}

    public static ProductDto toDto(final Product product) {
        return new ProductDto(
                product.getId(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public static Product toEntity(final ProductDto dto) {
        return new Product(
                dto.id(),
                dto.description(),
                dto.price()
        );
    }
}
