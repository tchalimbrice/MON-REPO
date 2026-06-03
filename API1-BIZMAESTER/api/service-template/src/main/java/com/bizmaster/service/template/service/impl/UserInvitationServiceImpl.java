package com.bizmaster.service.template.service.impl;

import com.bizmaster.service.template.dto.UserInvitationDto;
import com.bizmaster.service.template.entity.CompanyEntity;
import com.bizmaster.service.template.entity.CollaboratorEntity;
import com.bizmaster.service.template.entity.UserEntity;
import com.bizmaster.service.template.entity.UserInvitationEntity;
import com.bizmaster.service.template.mapper.UserInvitationMapper;
import com.bizmaster.service.template.repository.CollaboratorRepository;
import com.bizmaster.service.template.repository.CompanyRepository;
import com.bizmaster.service.template.repository.UserInvitationRepository;
import com.bizmaster.service.template.repository.UserRepository;
import com.bizmaster.service.template.service.UserInvitationService;
import java.time.Instant;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInvitationServiceImpl implements UserInvitationService {

    private final UserInvitationRepository invitationRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInvitationServiceImpl(UserInvitationRepository invitationRepository,
                                    CompanyRepository companyRepository,
                                    UserRepository userRepository,
                                    CollaboratorRepository collaboratorRepository,
                                    PasswordEncoder passwordEncoder) {
        this.invitationRepository = invitationRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserInvitationDto createInvitation(UserInvitationDto invitationDto) {
        CompanyEntity company = companyRepository.findById(invitationDto.getCompanyId())
            .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        UserEntity createdBy = userRepository.findById(invitationDto.getCreatedByUserId())
            .orElseThrow(() -> new IllegalArgumentException("Creator user not found"));

        UserInvitationEntity entity = new UserInvitationEntity(
            company,
            invitationDto.getInvitedEmail(),
            invitationDto.getFirstName(),
            invitationDto.getLastName(),
            invitationDto.getPosition(),
            createdBy
        );

        UserInvitationEntity saved = invitationRepository.save(entity);
        return UserInvitationMapper.toDto(saved);
    }

    @Override
    public UserInvitationDto getInvitationByToken(String token) {
        return invitationRepository.findByInvitationToken(token)
            .map(UserInvitationMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));
    }

    @Override
    public UserInvitationDto getInvitationById(Long invitationId) {
        return invitationRepository.findById(invitationId)
            .map(UserInvitationMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));
    }

    @Override
    public List<UserInvitationDto> getInvitationsByCompanyId(Long companyId) {
        return invitationRepository.findByCompanyId(companyId).stream()
            .map(UserInvitationMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserInvitationDto> getPendingInvitations(Long companyId) {
        return invitationRepository.findByStatus(UserInvitationEntity.InvitationStatus.PENDING)
            .stream()
            .filter(inv -> inv.getCompany().getId().equals(companyId))
            .map(UserInvitationMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserInvitationDto acceptInvitation(String token, String password, String confirmPassword, Long userId) {
        UserInvitationEntity entity = invitationRepository.findByInvitationToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invitation not found or expired"));

        if (entity.isExpired() || entity.getStatus() != UserInvitationEntity.InvitationStatus.PENDING) {
            throw new IllegalArgumentException("Invitation is no longer valid");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        CompanyEntity company = entity.getCompany();
        UserEntity acceptingUser = resolveAcceptingUser(entity, userId, password, company);

        if (acceptingUser.getCompany() == null) {
            acceptingUser.setCompany(company);
        }
        acceptingUser.setPassword(passwordEncoder.encode(password));
        acceptingUser.setPasswordChangeRequired(true);
        acceptingUser.setActive(true);
        acceptingUser.setDomain(company.getDomain());
        acceptingUser.setUpdatedAt(Instant.now());
        acceptingUser = userRepository.save(acceptingUser);

        if (collaboratorRepository.findByCompanyIdAndUserId(company.getId(), acceptingUser.getId()).isEmpty()) {
            CollaboratorEntity collaborator = new CollaboratorEntity(company, acceptingUser, entity.getPosition());
            collaborator.setStatus(CollaboratorEntity.CollaboratorStatus.ACTIVE);
            collaborator.setDataAccess(true);
            collaborator.setReportAccess(true);
            collaborator.setSettingsAccess(false);
            collaboratorRepository.save(collaborator);
        }

        entity.setStatus(UserInvitationEntity.InvitationStatus.ACCEPTED);
        entity.setAcceptedByUser(acceptingUser);
        entity.setAcceptedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        UserInvitationEntity saved = invitationRepository.save(entity);
        return UserInvitationMapper.toDto(saved);
    }

    private UserEntity resolveAcceptingUser(UserInvitationEntity invitation, Long userId, String password, CompanyEntity company) {
        if (userId != null && userId > 0) {
            return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        }

        return userRepository.findByEmail(invitation.getInvitedEmail())
            .orElseGet(() -> {
                UserEntity user = new UserEntity();
                user.setUsername(buildUsername(invitation.getInvitedEmail(), invitation.getFirstName(), invitation.getLastName()));
                user.setEmail(invitation.getInvitedEmail());
                user.setFirstName(invitation.getFirstName());
                user.setLastName(invitation.getLastName());
                user.setCompany(company);
                user.setDomain(company.getDomain());
                user.setRole(UserEntity.UserRole.COLLABORATOR);
                user.setActive(true);
                user.setPasswordChangeRequired(true);
                user.setPassword(passwordEncoder.encode(password));
                user.setCreatedAt(Instant.now());
                user.setUpdatedAt(Instant.now());
                return userRepository.save(user);
            });
    }

    private String buildUsername(String email, String firstName, String lastName) {
        String base = email != null && email.contains("@")
            ? email.substring(0, email.indexOf('@'))
            : (firstName + "." + lastName).toLowerCase().replace(' ', '.');
        String username = base.replaceAll("[^a-zA-Z0-9._-]", "").toLowerCase();
        if (username.isBlank()) {
            username = "user";
        }
        String candidate = username;
        int suffix = 1;
        while (userRepository.findByUsername(candidate).isPresent()) {
            candidate = username + suffix++;
        }
        return candidate;
    }

    @Override
    @Transactional
    public void revokeInvitation(Long invitationId) {
        UserInvitationEntity entity = invitationRepository.findById(invitationId)
            .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        entity.setStatus(UserInvitationEntity.InvitationStatus.REVOKED);
        entity.setUpdatedAt(Instant.now());
        invitationRepository.save(entity);
    }

    @Override
    @Transactional
    public void expireOldInvitations() {
        invitationRepository.findByStatus(UserInvitationEntity.InvitationStatus.PENDING)
            .stream()
            .filter(UserInvitationEntity::isExpired)
            .forEach(entity -> {
                entity.setStatus(UserInvitationEntity.InvitationStatus.EXPIRED);
                entity.setUpdatedAt(Instant.now());
                invitationRepository.save(entity);
            });
    }

    @Override
    public int getPendingInvitationCount(Long companyId) {
        return (int) invitationRepository.findByStatus(UserInvitationEntity.InvitationStatus.PENDING)
            .stream()
            .filter(inv -> inv.getCompany().getId().equals(companyId))
            .filter(inv -> !inv.isExpired())
            .count();
    }

    @Override
    public boolean isInvitationValid(String token) {
        return invitationRepository.findByInvitationToken(token)
            .map(inv -> inv.getStatus() == UserInvitationEntity.InvitationStatus.PENDING && !inv.isExpired())
            .orElse(false);
    }

    @Override
    public boolean isInvitationExpired(String token) {
        return invitationRepository.findByInvitationToken(token)
            .map(UserInvitationEntity::isExpired)
            .orElse(true);
    }
}
