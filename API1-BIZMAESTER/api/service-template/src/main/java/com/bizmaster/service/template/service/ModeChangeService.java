package com.bizmaster.service.template.service;

import com.bizmaster.service.template.dto.ModeChangeHistoryDto;
import java.util.List;

public interface ModeChangeService {
    ModeChangeHistoryDto initiateModeSwitching(Long companyId, String newMode, Long userId);
    ModeChangeHistoryDto updateModeChangeStatus(Long changeHistoryId, String status);
    ModeChangeHistoryDto completeModeChange(Long changeHistoryId);
    ModeChangeHistoryDto failModeChange(Long changeHistoryId, String errorMessage);
    ModeChangeHistoryDto getModeChangeHistoryById(Long historyId);
    List<ModeChangeHistoryDto> getModeChangeHistoryByCompany(Long companyId);
    List<ModeChangeHistoryDto> getLastModeChangeByCompany(Long companyId, int limit);
    void transferDataToCollaborativeMode(Long companyId, Long changeHistoryId);
    void createDefaultCollaboratorRoles(Long companyId);
}
