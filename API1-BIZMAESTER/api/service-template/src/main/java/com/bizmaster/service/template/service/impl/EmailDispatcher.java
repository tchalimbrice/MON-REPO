package com.bizmaster.service.template.service.impl;

import com.bizmaster.service.template.entity.EmailLogEntity;
import com.bizmaster.service.template.repository.EmailLogRepository;
import jakarta.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.List;
import org.springframework.core.env.Environment;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(JavaMailSender.class)
public class EmailDispatcher {

    private final EmailLogRepository emailLogRepository;
    private final JavaMailSender javaMailSender;
    private final Environment env;

    public EmailDispatcher(EmailLogRepository emailLogRepository, JavaMailSender javaMailSender, Environment env) {
        this.emailLogRepository = emailLogRepository;
        this.javaMailSender = javaMailSender;
        this.env = env;
    }

    @Scheduled(fixedDelayString = "${email.dispatcher.delay-ms:10000}")
    public void dispatchPendingEmails() {
        List<EmailLogEntity> pending = emailLogRepository.findByStatus(EmailLogEntity.EmailStatus.PENDING);
        if (pending == null || pending.isEmpty()) {
            return;
        }

        int limit = Math.min(pending.size(), Integer.parseInt(env.getProperty("email.dispatcher.batch-size", "20")));
        for (int i = 0; i < limit; i++) {
            EmailLogEntity e = pending.get(i);
            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
                helper.setTo(e.getRecipientEmail());
                helper.setSubject(e.getSubject());
                String content = (e.getHtmlBody() != null && !e.getHtmlBody().isEmpty()) ? e.getHtmlBody() : e.getBody();
                helper.setText(content == null ? "" : content, true);
                String from = env.getProperty("mail.from", env.getProperty("spring.mail.username", "no-reply@bizmaster.local"));
                helper.setFrom(from);

                javaMailSender.send(message);

                e.setStatus(EmailLogEntity.EmailStatus.SENT);
                e.setSentAt(Instant.now());
                e.setErrorMessage(null);
            } catch (Exception ex) {
                e.setStatus(EmailLogEntity.EmailStatus.FAILED);
                e.setFailedAt(Instant.now());
                e.setRetryCount(e.getRetryCount() + 1);
                e.setErrorMessage(ex.getMessage());
            }
            e.setUpdatedAt(Instant.now());
            emailLogRepository.save(e);
        }
    }
}
