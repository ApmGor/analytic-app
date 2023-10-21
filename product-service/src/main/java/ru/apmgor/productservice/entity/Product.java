package ru.apmgor.productservice.entity;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table(name = "products")
public class Product {
    @Id
    Integer id;
    String description;
    Integer price;
}
