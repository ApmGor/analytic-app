package ru.apmgor.analyticservice.entity;

import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Value
@Table(name = "product_views")
public class AnalyticProduct {
    @Id
    Integer productId;
    @With Integer count;
}
