package com.bizmaster.service.template.mapper;

import com.bizmaster.service.template.dto.ProductDto;
import com.bizmaster.service.template.entity.CompanyEntity;
import com.bizmaster.service.template.entity.ProductEntity;

import java.time.Instant;

public class ProductMapper {
    public static ProductDto toDto(ProductEntity entity) {
        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setCompanyId(entity.getCompany().getId());
        dto.setReference(entity.getReference());
        dto.setName(entity.getName());
        dto.setCategory(entity.getCategory());
        dto.setPurchasePrice(entity.getPurchasePrice());
        dto.setSalePrice(entity.getSalePrice());
        dto.setStock(entity.getStock());
        dto.setMinStock(entity.getMinStock());
        dto.setSupplier(entity.getSupplier());
        dto.setUnit(entity.getUnit());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static ProductEntity toEntity(ProductDto dto, CompanyEntity company) {
        ProductEntity entity = new ProductEntity();
        entity.setCompany(company);
        entity.setReference(dto.getReference());
        entity.setName(dto.getName());
        entity.setCategory(dto.getCategory());
        entity.setPurchasePrice(dto.getPurchasePrice());
        entity.setSalePrice(dto.getSalePrice());
        entity.setStock(dto.getStock());
        entity.setMinStock(dto.getMinStock());
        entity.setSupplier(dto.getSupplier());
        entity.setUnit(dto.getUnit());
        entity.setCreatedAt(Instant.now());
        return entity;
    }
}
