package com.bizmaster.service.template.repository;

import com.bizmaster.service.template.entity.ModeChangeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeChangeHistoryRepository extends JpaRepository<ModeChangeHistoryEntity, Long> {
    List<ModeChangeHistoryEntity> findByCompanyId(Long companyId);
    List<ModeChangeHistoryEntity> findByCompanyIdOrderByCreatedAtDesc(Long companyId);
    List<ModeChangeHistoryEntity> findByStatus(ModeChangeHistoryEntity.ChangeStatus status);
}
