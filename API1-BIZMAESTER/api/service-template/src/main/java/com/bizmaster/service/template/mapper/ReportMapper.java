package com.bizmaster.service.template.mapper;

import com.bizmaster.service.template.dto.ReportDto;
import com.bizmaster.service.template.entity.CompanyEntity;
import com.bizmaster.service.template.entity.ReportEntity;

import java.time.Instant;
import java.util.Base64;

public class ReportMapper {
    public static ReportDto toDto(ReportEntity entity) {
        ReportDto dto = new ReportDto();
        dto.setId(entity.getId());
        dto.setCompanyId(entity.getCompany().getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setGeneratedBy(entity.getGeneratedBy());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        if (entity.getPdfContent() != null) {
            dto.setPdfBase64(Base64.getEncoder().encodeToString(entity.getPdfContent()));
        }
        return dto;
    }

    public static ReportEntity toEntity(ReportDto dto, CompanyEntity company) {
        ReportEntity entity = new ReportEntity();
        entity.setCompany(company);
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setGeneratedBy(dto.getGeneratedBy());
        entity.setDescription(dto.getDescription());
        if (dto.getPdfBase64() != null) {
            entity.setPdfContent(Base64.getDecoder().decode(dto.getPdfBase64()));
        }
        entity.setCreatedAt(Instant.now());
        return entity;
    }
}
