package com.azilzor.productsmicroservice.service.impl;

import com.azilzor.productsmicroservice.dto.CreateProductDto;
import com.azilzor.productsmicroservice.kafka.KafkaConstants;
import com.azilzor.productsmicroservice.kafka.ProductCreatedEvent;
import com.azilzor.productsmicroservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Override
    public String createProduct(CreateProductDto createProductDto) {
        var productId = UUID.randomUUID().toString();
        var event = new ProductCreatedEvent(
                productId, createProductDto.name(), createProductDto.price(), createProductDto.quantity());

        var resultCompletableFuture = kafkaTemplate.send(KafkaConstants.PRODUCTS_CREATED_TOPIC, productId, event);
        resultCompletableFuture.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Error occurred while sending product created event", exception);
            } else {
                log.info("Product created event sent successfully: {}", result.getRecordMetadata());
            }
        });
//        resultCompletableFuture.join(); // wait for the result to complete in a blocking way

        return productId;
    }
}
