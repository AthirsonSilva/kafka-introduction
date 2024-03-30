package com.azilzor.productsmicroservice.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEvent {
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
