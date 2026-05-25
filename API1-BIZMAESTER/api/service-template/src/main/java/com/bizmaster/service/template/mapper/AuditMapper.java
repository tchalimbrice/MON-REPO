package com.bizmaster.service.template.mapper;

import com.bizmaster.service.template.dto.AuditDto;
import com.bizmaster.service.template.entity.AuditLogEntity;

public class AuditMapper {
    public static AuditDto toDto(AuditLogEntity entity) {
        AuditDto dto = new AuditDto();
        dto.setId(entity.getId());
        dto.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        dto.setAction(entity.getAction());
        dto.setUsername(entity.getUsername());
        dto.setDetails(entity.getDetails());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
