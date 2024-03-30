package com.azilzor.productsmicroservice.dto;

import java.math.BigDecimal;

public record CreateProductDto(
        String name,
        BigDecimal price,
        Integer quantity
) {
}
