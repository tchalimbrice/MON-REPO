package com.bizmaster.service.template.controller;

import com.bizmaster.service.template.dto.ApiResponse;
import com.bizmaster.service.template.dto.EmailLogDto;
import com.bizmaster.service.template.service.EmailService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
@Validated
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmailLogDto>>> listByCompany(@RequestParam Long companyId) {
        List<EmailLogDto> list = emailService.getEmailsByCompanyId(companyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Email logs fetched", list));
    }

    @GetMapping("/failed")
    public ResponseEntity<ApiResponse<List<EmailLogDto>>> failed() {
        List<EmailLogDto> list = emailService.getFailedEmails();
        return ResponseEntity.ok(new ApiResponse<>(true, "Failed emails fetched", list));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<EmailLogDto>>> pending() {
        List<EmailLogDto> list = emailService.getPendingEmails();
        return ResponseEntity.ok(new ApiResponse<>(true, "Pending emails fetched", list));
    }

    @PostMapping("/retry")
    public ResponseEntity<ApiResponse<Void>> retryFailed() {
        emailService.retryFailedEmails();
        return ResponseEntity.ok(new ApiResponse<>(true, "Retry queued"));
    }
}
