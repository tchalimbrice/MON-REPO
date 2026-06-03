package com.bizmaster.service.template.repository;

import com.bizmaster.service.template.entity.EmailLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLogEntity, Long> {
    List<EmailLogEntity> findByRecipientEmail(String email);
    List<EmailLogEntity> findByStatus(EmailLogEntity.EmailStatus status);
    List<EmailLogEntity> findByCompanyId(Long companyId);
    List<EmailLogEntity> findByStatusAndCreatedAtBefore(EmailLogEntity.EmailStatus status, Instant before);
    List<EmailLogEntity> findByEmailTypeAndCompanyId(EmailLogEntity.EmailType emailType, Long companyId);
}
