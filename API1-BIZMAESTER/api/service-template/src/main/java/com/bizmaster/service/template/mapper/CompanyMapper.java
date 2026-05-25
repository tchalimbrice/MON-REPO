package com.bizmaster.service.template.mapper;

import com.bizmaster.service.template.dto.CompanyDto;
import com.bizmaster.service.template.entity.CompanyEntity;

import java.time.Instant;

public class CompanyMapper {
    public static CompanyDto toDto(CompanyEntity entity) {
        CompanyDto dto = new CompanyDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setManagerName(entity.getManagerName());
        dto.setMode(entity.getMode());
        dto.setDomain(entity.getDomain());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static CompanyEntity toEntity(CompanyDto dto) {
        CompanyEntity entity = new CompanyEntity();
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setManagerName(dto.getManagerName());
        entity.setMode(dto.getMode());
        entity.setDomain(dto.getDomain());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return entity;
    }
}
