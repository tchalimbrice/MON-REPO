package com.bizmaster.service.template.repository;

import com.bizmaster.service.template.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByEmailContainingIgnoreCase(String emailFragment);
    List<UserEntity> findByUsernameContainingIgnoreCase(String usernameFragment);
    List<UserEntity> findByCompanyId(Long companyId);
    List<UserEntity> findByDomain(String domain);
    List<UserEntity> findByCompanyIdAndActive(Long companyId, boolean active);
}
