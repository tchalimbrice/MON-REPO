package com.bizmaster.service.template.service.impl;

import com.bizmaster.service.template.dto.ModeChangeHistoryDto;
import com.bizmaster.service.template.entity.CollaboratorEntity;
import com.bizmaster.service.template.entity.CompanyEntity;
import com.bizmaster.service.template.entity.ModeChangeHistoryEntity;
import com.bizmaster.service.template.entity.UserEntity;
import com.bizmaster.service.template.mapper.ModeChangeHistoryMapper;
import com.bizmaster.service.template.repository.CollaboratorRepository;
import com.bizmaster.service.template.repository.CompanyRepository;
import com.bizmaster.service.template.repository.ModeChangeHistoryRepository;
import com.bizmaster.service.template.repository.ProductRepository;
import com.bizmaster.service.template.repository.ReportRepository;
import com.bizmaster.service.template.repository.UserRepository;
import com.bizmaster.service.template.service.ModeChangeService;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModeChangeServiceImpl implements ModeChangeService {

    private final ModeChangeHistoryRepository modeChangeRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final ProductRepository productRepository;
    private final ReportRepository reportRepository;

    public ModeChangeServiceImpl(ModeChangeHistoryRepository modeChangeRepository,
                               CompanyRepository companyRepository,
                               UserRepository userRepository,
                               CollaboratorRepository collaboratorRepository,
                               ProductRepository productRepository,
                               ReportRepository reportRepository) {
        this.modeChangeRepository = modeChangeRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.productRepository = productRepository;
        this.reportRepository = reportRepository;
    }

    @Override
    @Transactional
    public ModeChangeHistoryDto initiateModeSwitching(Long companyId, String newMode, Long userId) {
        CompanyEntity company = companyRepository.findById(companyId)
            .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String oldMode = company.getMode();
        ModeChangeHistoryEntity entity = new ModeChangeHistoryEntity(company, oldMode, newMode, user);
        entity.setStatus(ModeChangeHistoryEntity.ChangeStatus.INITIATED);

        ModeChangeHistoryEntity saved = modeChangeRepository.save(entity);
        return ModeChangeHistoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ModeChangeHistoryDto updateModeChangeStatus(Long changeHistoryId, String status) {
        ModeChangeHistoryEntity entity = modeChangeRepository.findById(changeHistoryId)
            .orElseThrow(() -> new IllegalArgumentException("Mode change history not found"));

        entity.setStatus(ModeChangeHistoryEntity.ChangeStatus.valueOf(status));
        entity.setUpdatedAt(Instant.now());

        ModeChangeHistoryEntity saved = modeChangeRepository.save(entity);
        return ModeChangeHistoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ModeChangeHistoryDto completeModeChange(Long changeHistoryId) {
        ModeChangeHistoryEntity entity = modeChangeRepository.findById(changeHistoryId)
            .orElseThrow(() -> new IllegalArgumentException("Mode change history not found"));

        entity.setStatus(ModeChangeHistoryEntity.ChangeStatus.COMPLETED);
        entity.setCompletedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        // Update company mode
        CompanyEntity company = entity.getCompany();
        company.setMode(entity.getNewMode());
        company.setUpdatedAt(Instant.now());
        companyRepository.save(company);

        transferDataToCollaborativeMode(company.getId(), entity.getId());
        createDefaultCollaboratorRoles(company.getId());

        ModeChangeHistoryEntity saved = modeChangeRepository.save(entity);
        return ModeChangeHistoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ModeChangeHistoryDto failModeChange(Long changeHistoryId, String errorMessage) {
        ModeChangeHistoryEntity entity = modeChangeRepository.findById(changeHistoryId)
            .orElseThrow(() -> new IllegalArgumentException("Mode change history not found"));

        entity.setStatus(ModeChangeHistoryEntity.ChangeStatus.FAILED);
        entity.setNotes(errorMessage);
        entity.setUpdatedAt(Instant.now());

        ModeChangeHistoryEntity saved = modeChangeRepository.save(entity);
        return ModeChangeHistoryMapper.toDto(saved);
    }

    @Override
    public ModeChangeHistoryDto getModeChangeHistoryById(Long historyId) {
        return modeChangeRepository.findById(historyId)
            .map(ModeChangeHistoryMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Mode change history not found"));
    }

    @Override
    public List<ModeChangeHistoryDto> getModeChangeHistoryByCompany(Long companyId) {
        return modeChangeRepository.findByCompanyId(companyId).stream()
            .map(ModeChangeHistoryMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<ModeChangeHistoryDto> getLastModeChangeByCompany(Long companyId, int limit) {
        return modeChangeRepository.findByCompanyIdOrderByCreatedAtDesc(companyId).stream()
            .limit(limit)
            .map(ModeChangeHistoryMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void transferDataToCollaborativeMode(Long companyId, Long changeHistoryId) {
        ModeChangeHistoryEntity history = modeChangeRepository.findById(changeHistoryId)
            .orElseThrow(() -> new IllegalArgumentException("Mode change history not found"));

        long transferred = productRepository.countByCompanyId(companyId)
            + reportRepository.countByCompanyId(companyId);
        history.setDataRecordsTransferred((int) transferred);
        modeChangeRepository.save(history);
    }

    @Override
    @Transactional
    public void createDefaultCollaboratorRoles(Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
            .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        int created = 0;
        for (UserEntity user : userRepository.findByCompanyId(companyId)) {
            if (collaboratorRepository.findByCompanyIdAndUserId(companyId, user.getId()).isPresent()) {
                continue;
            }
            CollaboratorEntity collaborator = new CollaboratorEntity(company, user, defaultPositionFor(user));
            collaborator.setStatus(CollaboratorEntity.CollaboratorStatus.ACTIVE);
            collaborator.setDataAccess(true);
            collaborator.setReportAccess(true);
            collaborator.setSettingsAccess("CEO".equalsIgnoreCase(user.getRole() != null ? user.getRole().name() : ""));
            collaboratorRepository.save(collaborator);
            created++;
        }

        ModeChangeHistoryEntity latest = modeChangeRepository.findByCompanyIdOrderByCreatedAtDesc(companyId).stream().findFirst().orElse(null);
        if (latest != null) {
            latest.setCollaboratorsAdded(created);
            latest.setUpdatedAt(Instant.now());
            modeChangeRepository.save(latest);
        }
    }

    private String defaultPositionFor(UserEntity user) {
        if (user.getRole() == null) {
            return "Collaborateur";
        }
        return switch (user.getRole()) {
            case CEO -> "Chef d'entreprise";
            case ADMIN -> "Administrateur";
            case COLLABORATOR -> "Collaborateur";
        };
    }
}
