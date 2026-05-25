package com.bizmaster.service.template.repository;

import com.bizmaster.service.template.entity.ReportEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findByCompanyId(Long companyId);

    long countByCompanyId(Long companyId);

    Optional<ReportEntity> findByIdAndCompanyId(Long id, Long companyId);

    void deleteByIdAndCompanyId(Long id, Long companyId);
}
