package com.bizmaster.service.template.repository;

import com.bizmaster.service.template.entity.PageAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageAccessRepository extends JpaRepository<PageAccessEntity, Long> {
    List<PageAccessEntity> findByCollaboratorId(Long collaboratorId);
    List<PageAccessEntity> findByCollaboratorIdAndPageName(Long collaboratorId, String pageName);
}
