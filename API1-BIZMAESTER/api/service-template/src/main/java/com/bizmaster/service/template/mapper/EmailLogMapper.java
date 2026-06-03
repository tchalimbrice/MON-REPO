package com.bizmaster.service.template.mapper;

import com.bizmaster.service.template.dto.EmailLogDto;
import com.bizmaster.service.template.entity.EmailLogEntity;

public class EmailLogMapper {

    public static EmailLogDto toDto(EmailLogEntity entity) {
        if (entity == null) {
            return null;
        }
        EmailLogDto dto = new EmailLogDto();
        dto.setId(entity.getId());
        dto.setRecipientEmail(entity.getRecipientEmail());
        dto.setSubject(entity.getSubject());
        dto.setBody(entity.getBody());
        dto.setHtmlBody(entity.getHtmlBody());
        dto.setEmailType(entity.getEmailType() != null ? entity.getEmailType().name() : null);
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        dto.setRelatedEntity(entity.getRelatedEntity());
        dto.setRelatedEntityId(entity.getRelatedEntityId());
        dto.setErrorMessage(entity.getErrorMessage());
        dto.setRetryCount(entity.getRetryCount());
        dto.setSentAt(entity.getSentAt());
        dto.setFailedAt(entity.getFailedAt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static EmailLogEntity toEntity(EmailLogDto dto) {
        if (dto == null) {
            return null;
        }
        EmailLogEntity entity = new EmailLogEntity();
        entity.setId(dto.getId());
        entity.setRecipientEmail(dto.getRecipientEmail());
        entity.setSubject(dto.getSubject());
        entity.setBody(dto.getBody());
        entity.setHtmlBody(dto.getHtmlBody());
        entity.setRelatedEntity(dto.getRelatedEntity());
        entity.setRelatedEntityId(dto.getRelatedEntityId());
        entity.setErrorMessage(dto.getErrorMessage());
        entity.setRetryCount(dto.getRetryCount());
        entity.setSentAt(dto.getSentAt());
        entity.setFailedAt(dto.getFailedAt());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        if (dto.getEmailType() != null) {
            entity.setEmailType(EmailLogEntity.EmailType.valueOf(dto.getEmailType()));
        }
        if (dto.getStatus() != null) {
            entity.setStatus(EmailLogEntity.EmailStatus.valueOf(dto.getStatus()));
        }
        return entity;
    }
}
