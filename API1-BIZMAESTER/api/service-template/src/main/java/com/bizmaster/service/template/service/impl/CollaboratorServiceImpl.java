package com.bizmaster.service.template.service.impl;

import com.bizmaster.service.template.dto.CollaboratorDto;
import com.bizmaster.service.template.entity.CollaboratorEntity;
import com.bizmaster.service.template.entity.CompanyEntity;
import com.bizmaster.service.template.entity.UserEntity;
import com.bizmaster.service.template.mapper.CollaboratorMapper;
import com.bizmaster.service.template.repository.CollaboratorRepository;
import com.bizmaster.service.template.repository.CompanyRepository;
import com.bizmaster.service.template.repository.UserRepository;
import com.bizmaster.service.template.service.CollaboratorService;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollaboratorServiceImpl implements CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CollaboratorServiceImpl(CollaboratorRepository collaboratorRepository,
                                  CompanyRepository companyRepository,
                                  UserRepository userRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CollaboratorDto addCollaborator(CollaboratorDto collaboratorDto) {
        CompanyEntity company = companyRepository.findById(collaboratorDto.getCompanyId())
            .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        
        UserEntity user = userRepository.findById(collaboratorDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if already exists
        if (collaboratorRepository.findByCompanyIdAndUserId(company.getId(), user.getId()).isPresent()) {
            throw new IllegalArgumentException("Collaborator already exists for this company");
        }

        CollaboratorEntity entity = new CollaboratorEntity(company, user, collaboratorDto.getPosition());
        entity.setDataAccess(collaboratorDto.isDataAccess());
        entity.setReportAccess(collaboratorDto.isReportAccess());
        entity.setSettingsAccess(collaboratorDto.isSettingsAccess());
        entity.setStatus(CollaboratorEntity.CollaboratorStatus.ACTIVE);

        CollaboratorEntity saved = collaboratorRepository.save(entity);
        return CollaboratorMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CollaboratorDto updateCollaborator(Long collaboratorId, CollaboratorDto collaboratorDto) {
        CollaboratorEntity entity = collaboratorRepository.findById(collaboratorId)
            .orElseThrow(() -> new IllegalArgumentException("Collaborator not found"));

        entity.setPosition(collaboratorDto.getPosition());
        entity.setDataAccess(collaboratorDto.isDataAccess());
        entity.setReportAccess(collaboratorDto.isReportAccess());
        entity.setSettingsAccess(collaboratorDto.isSettingsAccess());
        entity.setNotes(collaboratorDto.getNotes());
        entity.setUpdatedAt(Instant.now());

        CollaboratorEntity saved = collaboratorRepository.save(entity);
        return CollaboratorMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void removeCollaborator(Long collaboratorId) {
        CollaboratorEntity entity = collaboratorRepository.findById(collaboratorId)
            .orElseThrow(() -> new IllegalArgumentException("Collaborator not found"));
        
        entity.setStatus(CollaboratorEntity.CollaboratorStatus.REMOVED);
        entity.setDateLeft(Instant.now());
        entity.setUpdatedAt(Instant.now());
        collaboratorRepository.save(entity);
    }

    @Override
    public CollaboratorDto getCollaboratorById(Long collaboratorId) {
        return collaboratorRepository.findById(collaboratorId)
            .map(CollaboratorMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Collaborator not found"));
    }

    @Override
    public List<CollaboratorDto> getCollaboratorsByCompanyId(Long companyId) {
        return collaboratorRepository.findByCompanyId(companyId).stream()
            .map(CollaboratorMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<CollaboratorDto> getCollaboratorsByUserId(Long userId) {
        return collaboratorRepository.findByUserId(userId).stream()
            .map(CollaboratorMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public CollaboratorDto getCollaboratorByCompanyAndUser(Long companyId, Long userId) {
        return collaboratorRepository.findByCompanyIdAndUserId(companyId, userId)
            .map(CollaboratorMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Collaborator not found"));
    }

    @Override
    public List<CollaboratorDto> getActiveCollaborators(Long companyId) {
        return collaboratorRepository.findByCompanyIdAndStatus(companyId, CollaboratorEntity.CollaboratorStatus.ACTIVE)
            .stream()
            .map(CollaboratorMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void suspendCollaborator(Long collaboratorId) {
        CollaboratorEntity entity = collaboratorRepository.findById(collaboratorId)
            .orElseThrow(() -> new IllegalArgumentException("Collaborator not found"));
        
        entity.setStatus(CollaboratorEntity.CollaboratorStatus.SUSPENDED);
        entity.setUpdatedAt(Instant.now());
        collaboratorRepository.save(entity);
    }

    @Override
    @Transactional
    public void activateCollaborator(Long collaboratorId) {
        CollaboratorEntity entity = collaboratorRepository.findById(collaboratorId)
            .orElseThrow(() -> new IllegalArgumentException("Collaborator not found"));
        
        entity.setStatus(CollaboratorEntity.CollaboratorStatus.ACTIVE);
        entity.setUpdatedAt(Instant.now());
        collaboratorRepository.save(entity);
    }

    @Override
    public int getCollaboratorCountByCompany(Long companyId) {
        return (int) collaboratorRepository.findByCompanyId(companyId).stream()
            .filter(c -> c.getStatus() != CollaboratorEntity.CollaboratorStatus.REMOVED)
            .count();
    }
}
