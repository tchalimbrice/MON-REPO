package com.bizmaster.service.template.service;

import com.bizmaster.service.template.dto.CollaboratorDto;
import java.util.List;

public interface CollaboratorService {
    CollaboratorDto addCollaborator(CollaboratorDto collaboratorDto);
    CollaboratorDto updateCollaborator(Long collaboratorId, CollaboratorDto collaboratorDto);
    void removeCollaborator(Long collaboratorId);
    CollaboratorDto getCollaboratorById(Long collaboratorId);
    List<CollaboratorDto> getCollaboratorsByCompanyId(Long companyId);
    List<CollaboratorDto> getCollaboratorsByUserId(Long userId);
    CollaboratorDto getCollaboratorByCompanyAndUser(Long companyId, Long userId);
    List<CollaboratorDto> getActiveCollaborators(Long companyId);
    void suspendCollaborator(Long collaboratorId);
    void activateCollaborator(Long collaboratorId);
    int getCollaboratorCountByCompany(Long companyId);
}
