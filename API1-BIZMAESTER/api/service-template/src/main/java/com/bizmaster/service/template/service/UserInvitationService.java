package com.bizmaster.service.template.service;

import com.bizmaster.service.template.dto.UserInvitationDto;
import java.util.List;

public interface UserInvitationService {
    UserInvitationDto createInvitation(UserInvitationDto invitationDto);
    UserInvitationDto getInvitationByToken(String token);
    UserInvitationDto getInvitationById(Long invitationId);
    List<UserInvitationDto> getInvitationsByCompanyId(Long companyId);
    List<UserInvitationDto> getPendingInvitations(Long companyId);
    UserInvitationDto acceptInvitation(String token, String password, String confirmPassword, Long userId);
    void revokeInvitation(Long invitationId);
    void expireOldInvitations();
    int getPendingInvitationCount(Long companyId);
    boolean isInvitationValid(String token);
    boolean isInvitationExpired(String token);
}
