package com.azilzor.productsmicroservice.exception;

import java.time.LocalDateTime;

public record ErrorMessage(
        LocalDateTime timestamp,
        String message,
        String details

) {
}
