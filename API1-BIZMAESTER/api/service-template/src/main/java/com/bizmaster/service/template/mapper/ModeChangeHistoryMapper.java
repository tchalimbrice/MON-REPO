package com.bizmaster.service.template.mapper;

import com.bizmaster.service.template.dto.ModeChangeHistoryDto;
import com.bizmaster.service.template.entity.ModeChangeHistoryEntity;

public class ModeChangeHistoryMapper {

    public static ModeChangeHistoryDto toDto(ModeChangeHistoryEntity entity) {
        if (entity == null) {
            return null;
        }
        ModeChangeHistoryDto dto = new ModeChangeHistoryDto();
        dto.setId(entity.getId());
        dto.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        dto.setOldMode(entity.getOldMode());
        dto.setNewMode(entity.getNewMode());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setInitiatedByUserId(entity.getInitiatedByUser() != null ? entity.getInitiatedByUser().getId() : null);
        dto.setNotes(entity.getNotes());
        dto.setCollaboratorsAdded(entity.getCollaboratorsAdded());
        dto.setDataRecordsTransferred(entity.getDataRecordsTransferred());
        dto.setCompletedAt(entity.getCompletedAt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static ModeChangeHistoryEntity toEntity(ModeChangeHistoryDto dto) {
        if (dto == null) {
            return null;
        }
        ModeChangeHistoryEntity entity = new ModeChangeHistoryEntity();
        entity.setId(dto.getId());
        entity.setOldMode(dto.getOldMode());
        entity.setNewMode(dto.getNewMode());
        entity.setNotes(dto.getNotes());
        entity.setCollaboratorsAdded(dto.getCollaboratorsAdded());
        entity.setDataRecordsTransferred(dto.getDataRecordsTransferred());
        entity.setCompletedAt(dto.getCompletedAt());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        if (dto.getStatus() != null) {
            entity.setStatus(ModeChangeHistoryEntity.ChangeStatus.valueOf(dto.getStatus()));
        }
        return entity;
    }
}
