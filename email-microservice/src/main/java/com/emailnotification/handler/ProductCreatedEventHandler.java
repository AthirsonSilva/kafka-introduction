package com.emailnotification.handler;

import com.emailnotification.entity.ProcessedEventEntity;
import com.emailnotification.error.NotRetryableException;
import com.emailnotification.error.RetryableException;
import com.emailnotification.event.ProductCreatedEvent;
import com.emailnotification.repository.ProcessedEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@Log4j2
@RequiredArgsConstructor
@KafkaListener(topics = "product-created-topic")
public class ProductCreatedEventHandler {

    private final RestTemplate restTemplate;
    private final ProcessedEntityRepository processedEntityRepository;

    @KafkaHandler
    @Transactional
    public void handle(
            @Payload ProductCreatedEvent productCreatedEvent,
            @Header("messageId") String messageId,
            @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        log.info("Received a new event: " + productCreatedEvent.name());

        if (processedEntityRepository.findByEventId(messageId) != null) {
            log.info("Already processed event: " + productCreatedEvent.name());
            return;
        }

        try {
            String requestUrl = "http://localhost:8082/response/200";
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);

            if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                log.info("Received response from a remote service: " + response.getBody());
            }
        } catch (ResourceAccessException ex) {
            log.error(ex.getMessage());
            throw new RetryableException(ex);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new NotRetryableException(ex);
        }

        log.info("Saving processed event: " + productCreatedEvent.name());

        try {
            var processedEntity = ProcessedEventEntity.builder()
                    .eventId(messageId)
                    .productId(productCreatedEvent.productId())
                    .build();
            processedEntityRepository.save(processedEntity);
        } catch (DataIntegrityViolationException ex) {
            log.error("Already processed event: " + productCreatedEvent.name());
            throw new NotRetryableException(ex);
        }
    }

}
