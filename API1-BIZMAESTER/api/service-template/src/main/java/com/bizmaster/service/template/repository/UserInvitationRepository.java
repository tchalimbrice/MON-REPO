package com.bizmaster.service.template.repository;

import com.bizmaster.service.template.entity.UserInvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInvitationRepository extends JpaRepository<UserInvitationEntity, Long> {
    Optional<UserInvitationEntity> findByInvitationToken(String token);
    List<UserInvitationEntity> findByCompanyId(Long companyId);
    List<UserInvitationEntity> findByStatus(UserInvitationEntity.InvitationStatus status);
    List<UserInvitationEntity> findByInvitedEmail(String email);
}
