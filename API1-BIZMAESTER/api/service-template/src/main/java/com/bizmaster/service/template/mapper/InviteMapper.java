package com.bizmaster.service.template.mapper;

import com.bizmaster.service.template.dto.InviteResponseDto;
import com.bizmaster.service.template.entity.InviteTokenEntity;

public class InviteMapper {
    public static InviteResponseDto toDto(InviteTokenEntity entity) {
        InviteResponseDto dto = new InviteResponseDto();
        dto.setToken(entity.getToken());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        dto.setCompanyName(entity.getCompany().getName());
        dto.setMode(entity.getMode());
        return dto;
    }
}
