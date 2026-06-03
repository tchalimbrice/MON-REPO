package com.bizmaster.service.template.mapper;

import com.bizmaster.service.template.dto.CollaboratorDto;
import com.bizmaster.service.template.entity.CollaboratorEntity;

public class CollaboratorMapper {

    public static CollaboratorDto toDto(CollaboratorEntity entity) {
        if (entity == null) {
            return null;
        }
        CollaboratorDto dto = new CollaboratorDto();
        dto.setId(entity.getId());
        dto.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setPosition(entity.getPosition());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setDataAccess(entity.isDataAccess());
        dto.setReportAccess(entity.isReportAccess());
        dto.setSettingsAccess(entity.isSettingsAccess());
        dto.setNotes(entity.getNotes());
        dto.setDateJoined(entity.getDateJoined());
        dto.setDateLeft(entity.getDateLeft());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static CollaboratorEntity toEntity(CollaboratorDto dto) {
        if (dto == null) {
            return null;
        }
        CollaboratorEntity entity = new CollaboratorEntity();
        entity.setId(dto.getId());
        entity.setPosition(dto.getPosition());
        entity.setDataAccess(dto.isDataAccess());
        entity.setReportAccess(dto.isReportAccess());
        entity.setSettingsAccess(dto.isSettingsAccess());
        entity.setNotes(dto.getNotes());
        entity.setDateJoined(dto.getDateJoined());
        entity.setDateLeft(dto.getDateLeft());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        if (dto.getStatus() != null) {
            entity.setStatus(CollaboratorEntity.CollaboratorStatus.valueOf(dto.getStatus()));
        }
        return entity;
    }
}
