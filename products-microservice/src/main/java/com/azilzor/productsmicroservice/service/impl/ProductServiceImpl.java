package com.azilzor.productsmicroservice.service.impl;

import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.azilzor.productsmicroservice.dto.CreateProductDto;
import com.azilzor.productsmicroservice.kafka.KafkaConstants;
import com.azilzor.productsmicroservice.kafka.ProductCreatedEvent;
import com.azilzor.productsmicroservice.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
        var record = new ProducerRecord<>(
                KafkaConstants.PRODUCTS_CREATED_TOPIC,
                productId,
                event);
        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        log.info("Publishing product created event: {}", event);

        var resultCompletableFuture = kafkaTemplate.send(record);
        resultCompletableFuture.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Error occurred while sending product created event", exception);
            }
        });

        return productId;
    }
}
