package com.bizmaster.service.template.service.impl;

import com.bizmaster.service.template.dto.EmailLogDto;
import com.bizmaster.service.template.entity.CompanyEntity;
import com.bizmaster.service.template.entity.EmailLogEntity;
import com.bizmaster.service.template.mapper.EmailLogMapper;
import com.bizmaster.service.template.repository.CompanyRepository;
import com.bizmaster.service.template.repository.EmailLogRepository;
import com.bizmaster.service.template.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailLogRepository emailLogRepository;
    private final CompanyRepository companyRepository;
    private final JavaMailSender mailSender;
    private final String mailFrom;

    public EmailServiceImpl(EmailLogRepository emailLogRepository, CompanyRepository companyRepository,
                            JavaMailSender mailSender,
                            @Value("${mail.from:no-reply@bizmaster.local}") String mailFrom) {
        this.emailLogRepository = emailLogRepository;
        this.companyRepository = companyRepository;
        this.mailSender = mailSender;
        this.mailFrom = mailFrom;
    }

    @Override
    @Transactional
    public EmailLogDto sendInvitationEmail(String recipientEmail, String firstName, String lastName, 
                                            String companyName, String invitationLink, Long companyId) {
        CompanyEntity company = null;
        if (companyId != null) {
            company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        }

        String subject = "Invitation à rejoindre " + companyName;
        String body = buildInvitationEmailBody(firstName, lastName, companyName, invitationLink);
        String htmlBody = buildInvitationEmailHtml(firstName, lastName, companyName, invitationLink);

        EmailLogEntity entity = new EmailLogEntity(recipientEmail, subject, body, htmlBody, 
            EmailLogEntity.EmailType.INVITATION, company);
        entity.setRelatedEntity("USER_INVITATION");

        EmailLogEntity saved = emailLogRepository.save(entity);
        try {
            sendMail(recipientEmail, subject, body, htmlBody);
            saved.setStatus(EmailLogEntity.EmailStatus.SENT);
            saved.setSentAt(Instant.now());
            saved.setErrorMessage(null);
        } catch (Exception ex) {
            saved.setStatus(EmailLogEntity.EmailStatus.FAILED);
            saved.setErrorMessage(ex.getMessage());
            saved.setFailedAt(Instant.now());
        }
        saved.setUpdatedAt(Instant.now());
        saved = emailLogRepository.save(saved);
        return EmailLogMapper.toDto(saved);
    }

    private void sendMail(String recipientEmail, String subject, String textBody, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(mailFrom);
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(textBody, htmlBody);
        mailSender.send(message);
    }

    @Override
    @Transactional
    public EmailLogDto sendPasswordResetEmail(String recipientEmail, String resetLink, Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
            .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        String subject = "Réinitialisation de votre mot de passe";
        String body = "Cliquez sur le lien suivant pour réinitialiser votre mot de passe: " + resetLink;
        String htmlBody = "<p>Cliquez sur le lien suivant pour réinitialiser votre mot de passe:</p>" +
            "<a href=\"" + resetLink + "\">Réinitialiser le mot de passe</a>";

        EmailLogEntity entity = new EmailLogEntity(recipientEmail, subject, body, htmlBody,
            EmailLogEntity.EmailType.PASSWORD_RESET, company);
        entity.setRelatedEntity("PASSWORD_RESET");

        EmailLogEntity saved = emailLogRepository.save(entity);
        return EmailLogMapper.toDto(saved);
    }

    @Override
    @Transactional
    public EmailLogDto sendNotificationEmail(String recipientEmail, String subject, String body, Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
            .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        EmailLogEntity entity = new EmailLogEntity(recipientEmail, subject, body, body,
            EmailLogEntity.EmailType.NOTIFICATION, company);
        entity.setRelatedEntity("NOTIFICATION");

        EmailLogEntity saved = emailLogRepository.save(entity);
        return EmailLogMapper.toDto(saved);
    }

    @Override
    @Transactional
    public EmailLogDto sendReportEmail(String recipientEmail, String reportTitle, String reportContent, Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
            .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        EmailLogEntity entity = new EmailLogEntity(recipientEmail, "Rapport: " + reportTitle, reportContent, reportContent,
            EmailLogEntity.EmailType.REPORT, company);
        entity.setRelatedEntity("REPORT");

        EmailLogEntity saved = emailLogRepository.save(entity);
        return EmailLogMapper.toDto(saved);
    }

    @Override
    public List<EmailLogDto> getEmailsByCompanyId(Long companyId) {
        return emailLogRepository.findByCompanyId(companyId).stream()
            .map(EmailLogMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmailLogDto> getFailedEmails() {
        return emailLogRepository.findByStatus(EmailLogEntity.EmailStatus.FAILED).stream()
            .map(EmailLogMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmailLogDto> getPendingEmails() {
        return emailLogRepository.findByStatus(EmailLogEntity.EmailStatus.PENDING).stream()
            .map(EmailLogMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void retryFailedEmails() {
        List<EmailLogEntity> failedEmails = emailLogRepository.findByStatus(EmailLogEntity.EmailStatus.FAILED);
        failedEmails.stream()
            .filter(EmailLogEntity::canRetry)
            .forEach(email -> {
                email.setRetryCount(email.getRetryCount() + 1);
                email.setStatus(EmailLogEntity.EmailStatus.PENDING);
                email.setUpdatedAt(Instant.now());
                emailLogRepository.save(email);
            });
    }

    @Override
    public EmailLogDto getEmailLogById(Long emailLogId) {
        return emailLogRepository.findById(emailLogId)
            .map(EmailLogMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Email log not found"));
    }

    @Override
    @Transactional
    public void logEmailEvent(EmailLogDto emailLogDto) {
        EmailLogEntity entity = EmailLogMapper.toEntity(emailLogDto);
        emailLogRepository.save(entity);
    }

    private String buildInvitationEmailBody(String firstName, String lastName, String companyName, String invitationLink) {
        return String.format(
            "Bonjour %s %s,\n\nVous avez été invité(e) à rejoindre %s.\n\n" +
            "Cliquez sur le lien suivant pour accepter votre invitation:\n%s\n\n" +
            "Cordialement,\nL'équipe BizMaster",
            firstName, lastName, companyName, invitationLink
        );
    }

    private String buildInvitationEmailHtml(String firstName, String lastName, String companyName, String invitationLink) {
        return String.format(
            "<html><body><p>Bonjour %s %s,</p>" +
            "<p>Vous avez été invité(e) à rejoindre <strong>%s</strong>.</p>" +
            "<p><a href=\"%s\" style=\"background-color: #007bff; color: white; padding: 10px 20px; " +
            "text-decoration: none; border-radius: 5px;\">Accepter l'invitation</a></p>" +
            "<p>Cordialement,<br>L'équipe BizMaster</p></body></html>",
            firstName, lastName, companyName, invitationLink
        );
    }
}
