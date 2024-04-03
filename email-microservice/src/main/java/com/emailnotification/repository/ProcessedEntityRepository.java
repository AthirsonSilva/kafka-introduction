package com.emailnotification.repository;

import com.emailnotification.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProcessedEntityRepository extends JpaRepository<ProcessedEventEntity, Long> {

    @Query(value = "SELECT * FROM ProcessedEventEntity p WHERE p.eventId = ?1", nativeQuery = true)
    ProcessedEventEntity findByEventId(String eventId);

}
