package com.bizmaster.service.template.repository;

import com.bizmaster.service.template.entity.CollaboratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollaboratorRepository extends JpaRepository<CollaboratorEntity, Long> {
    List<CollaboratorEntity> findByCompanyId(Long companyId);
    List<CollaboratorEntity> findByUserId(Long userId);
    Optional<CollaboratorEntity> findByCompanyIdAndUserId(Long companyId, Long userId);
    List<CollaboratorEntity> findByCompanyIdAndStatus(Long companyId, CollaboratorEntity.CollaboratorStatus status);
}
