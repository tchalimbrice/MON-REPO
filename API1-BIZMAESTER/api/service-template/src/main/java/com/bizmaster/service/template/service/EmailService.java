package com.bizmaster.service.template.service;

import com.bizmaster.service.template.dto.EmailLogDto;
import java.util.List;

public interface EmailService {
    EmailLogDto sendInvitationEmail(String recipientEmail, String firstName, String lastName, String companyName, String invitationLink, Long companyId);
    EmailLogDto sendPasswordResetEmail(String recipientEmail, String resetLink, Long companyId);
    EmailLogDto sendNotificationEmail(String recipientEmail, String subject, String body, Long companyId);
    EmailLogDto sendReportEmail(String recipientEmail, String reportTitle, String reportContent, Long companyId);
    List<EmailLogDto> getEmailsByCompanyId(Long companyId);
    List<EmailLogDto> getFailedEmails();
    List<EmailLogDto> getPendingEmails();
    void retryFailedEmails();
    EmailLogDto getEmailLogById(Long emailLogId);
    void logEmailEvent(EmailLogDto emailLogDto);
}
