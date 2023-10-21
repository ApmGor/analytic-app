package ru.apmgor.analyticservice.mapper;

import ru.apmgor.analyticservice.dto.AnalyticProductDto;
import ru.apmgor.analyticservice.entity.AnalyticProduct;

public class AnalyticMapper {
    private AnalyticMapper() {}

    public static AnalyticProductDto toDto(final AnalyticProduct product) {
        return new AnalyticProductDto(
                product.getProductId(),
                product.getCount()
        );
    }
}
