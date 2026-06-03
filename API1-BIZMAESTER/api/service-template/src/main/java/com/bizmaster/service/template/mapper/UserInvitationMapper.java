package com.bizmaster.service.template.mapper;

import com.bizmaster.service.template.dto.UserInvitationDto;
import com.bizmaster.service.template.entity.UserInvitationEntity;

public class UserInvitationMapper {

    public static UserInvitationDto toDto(UserInvitationEntity entity) {
        if (entity == null) {
            return null;
        }
        UserInvitationDto dto = new UserInvitationDto();
        dto.setId(entity.getId());
        dto.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        dto.setInvitedEmail(entity.getInvitedEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPosition(entity.getPosition());
        dto.setInvitationToken(entity.getInvitationToken());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setCreatedByUserId(entity.getCreatedByUser() != null ? entity.getCreatedByUser().getId() : null);
        dto.setAcceptedByUserId(entity.getAcceptedByUser() != null ? entity.getAcceptedByUser().getId() : null);
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setAcceptedAt(entity.getAcceptedAt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static UserInvitationEntity toEntity(UserInvitationDto dto) {
        if (dto == null) {
            return null;
        }
        UserInvitationEntity entity = new UserInvitationEntity();
        entity.setId(dto.getId());
        entity.setInvitedEmail(dto.getInvitedEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPosition(dto.getPosition());
        entity.setInvitationToken(dto.getInvitationToken());
        entity.setExpiresAt(dto.getExpiresAt());
        entity.setAcceptedAt(dto.getAcceptedAt());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        if (dto.getStatus() != null) {
            entity.setStatus(UserInvitationEntity.InvitationStatus.valueOf(dto.getStatus()));
        }
        return entity;
    }
}
