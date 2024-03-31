package com.coredomain;

import java.math.BigDecimal;

public record ProductCreatedEvent(
        String productId,
        String name,
        BigDecimal price,
        Integer quantity
) {
}

