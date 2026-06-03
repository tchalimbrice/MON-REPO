package com.bizmaster.service.template.controller;

import com.bizmaster.service.template.dto.ApiResponse;
import com.bizmaster.service.template.dto.AcceptInvitationRequest;
import com.bizmaster.service.template.dto.InviteCollaboratorRequest;
import com.bizmaster.service.template.dto.UserInvitationDto;
import com.bizmaster.service.template.dto.SendInvitationsRequest;
import com.bizmaster.service.template.service.EmailService;
import com.bizmaster.service.template.service.UserInvitationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invitations")
@Validated
public class InvitationController {

    private final UserInvitationService invitationService;
    private final EmailService emailService;

    public InvitationController(UserInvitationService invitationService, EmailService emailService) {
        this.invitationService = invitationService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserInvitationDto>> invite(@Valid @RequestBody InviteCollaboratorRequest req,
                                                                 @RequestParam Long companyId,
                                                                 @RequestParam Long createdByUserId) {
        UserInvitationDto dto = new UserInvitationDto();
        dto.setCompanyId(companyId);
        dto.setInvitedEmail(req.getInvitedEmail());
        dto.setFirstName(req.getFirstName());
        dto.setLastName(req.getLastName());
        dto.setPosition(req.getPosition());
        dto.setCreatedByUserId(createdByUserId);

        UserInvitationDto created = invitationService.createInvitation(dto);

        // Build a simple invitation link (frontend route placeholder)
        String invitationLink = String.format("%s/invite/accept?token=%s", "http://localhost:4200", created.getInvitationToken());
        emailService.sendInvitationEmail(created.getInvitedEmail(), created.getFirstName(), created.getLastName(), "BizMaster", invitationLink, created.getCompanyId());

        return ResponseEntity.ok(new ApiResponse<>(true, "Invitation created and email queued", created));
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> sendInvitations(@Valid @RequestBody SendInvitationsRequest request) {
        String companyName = request.getOwner() != null ? request.getOwner().getName() : "BizMaster";

        if (request.getOwner() != null) {
            SendInvitationsRequest.SendInvitationOwner owner = request.getOwner();
            String ownerName = owner.getName() != null ? owner.getName() : "BizMaster";
            String[] ownerParts = ownerName.trim().split(" ", 2);
            String firstName = ownerParts.length > 0 ? ownerParts[0] : "";
            String lastName = ownerParts.length > 1 ? ownerParts[1] : "";
            emailService.sendInvitationEmail(owner.getEmail(), firstName, lastName, companyName, owner.getUrl(), null);
        }

        for (SendInvitationsRequest.SendInvitationItem invitation : request.getInvitations()) {
            String[] nameParts = invitation.getName() != null ? invitation.getName().trim().split(" ", 2) : new String[] {"", ""};
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String lastName = nameParts.length > 1 ? nameParts[1] : "";
            emailService.sendInvitationEmail(invitation.getEmail(), firstName, lastName, companyName, invitation.getUrl(), null);
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Invitations envoyées", null));
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<ApiResponse<UserInvitationDto>> getByToken(@PathVariable String token) {
        UserInvitationDto dto = invitationService.getInvitationByToken(token);
        return ResponseEntity.ok(new ApiResponse<>(true, "Invitation fetched", dto));
    }

    @PostMapping("/accept")
    public ResponseEntity<ApiResponse<UserInvitationDto>> accept(@Valid @RequestBody AcceptInvitationRequest req, @RequestParam(required = false) Long userId) {
        UserInvitationDto accepted = invitationService.acceptInvitation(req.getInvitationToken(), req.getPassword(), req.getConfirmPassword(), userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Invitation accepted", accepted));
    }

    @PostMapping("/revoke")
    public ResponseEntity<ApiResponse<Void>> revoke(@RequestParam Long invitationId) {
        invitationService.revokeInvitation(invitationId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Invitation revoked"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserInvitationDto>>> list(@RequestParam Long companyId) {
        List<UserInvitationDto> list = invitationService.getInvitationsByCompanyId(companyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Invitations fetched", list));
    }
}
