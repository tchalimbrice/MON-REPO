package com.bizmaster.service.template.mapper;

import com.bizmaster.service.template.dto.PageAccessDto;
import com.bizmaster.service.template.entity.PageAccessEntity;

public class PageAccessMapper {

    public static PageAccessDto toDto(PageAccessEntity entity) {
        if (entity == null) {
            return null;
        }
        PageAccessDto dto = new PageAccessDto();
        dto.setId(entity.getId());
        dto.setCollaboratorId(entity.getCollaborator() != null ? entity.getCollaborator().getId() : null);
        dto.setPageName(entity.getPageName());
        dto.setCanView(entity.isCanView());
        dto.setCanCreate(entity.isCanCreate());
        dto.setCanEdit(entity.isCanEdit());
        dto.setCanDelete(entity.isCanDelete());
        dto.setNotes(entity.getNotes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static PageAccessEntity toEntity(PageAccessDto dto) {
        if (dto == null) {
            return null;
        }
        PageAccessEntity entity = new PageAccessEntity();
        entity.setId(dto.getId());
        entity.setPageName(dto.getPageName());
        entity.setCanView(dto.isCanView());
        entity.setCanCreate(dto.isCanCreate());
        entity.setCanEdit(dto.isCanEdit());
        entity.setCanDelete(dto.isCanDelete());
        entity.setNotes(dto.getNotes());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
