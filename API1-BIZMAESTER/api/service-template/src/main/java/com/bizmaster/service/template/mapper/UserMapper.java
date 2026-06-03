package com.bizmaster.service.template.mapper;

import com.bizmaster.service.template.dto.UserDto;
import com.bizmaster.service.template.entity.UserEntity;

public class UserMapper {

    public static UserDto toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        dto.setRole(entity.getRole() != null ? entity.getRole().name() : null);
        dto.setDomain(entity.getDomain());
        dto.setActive(entity.isActive());
        dto.setPasswordChangeRequired(entity.isPasswordChangeRequired());
        dto.setLastLogin(entity.getLastLogin());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static UserEntity toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPassword(dto.getPassword());
        entity.setDomain(dto.getDomain());
        entity.setActive(dto.isActive());
        entity.setPasswordChangeRequired(dto.isPasswordChangeRequired());
        entity.setLastLogin(dto.getLastLogin());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
