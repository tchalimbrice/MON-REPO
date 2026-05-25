package com.bizmaster.service.template.repository;

import com.bizmaster.service.template.entity.CompanyEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    List<CompanyEntity> findByDomainIgnoreCase(String domain);

    Optional<CompanyEntity> findByNameIgnoreCaseAndDomainIgnoreCase(String name, String domain);
}
