package com.bizmaster.service.template.repository;

import com.bizmaster.service.template.entity.InviteTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteTokenRepository extends JpaRepository<InviteTokenEntity, Long> {
    Optional<InviteTokenEntity> findByToken(String token);

    long countByCompanyId(Long companyId);
}
