package ru.apmgor.analyticservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_views")
public class AnalyticProduct implements Persistable<Integer> {
    @Id
    private Integer productId;
    @With
    private Long count;
    @Transient
    private boolean isNew;
    @Override
    public Integer getId() {
        return productId;
    }
    @Override
    public boolean isNew() {
        return isNew || productId == null;
    }
}
