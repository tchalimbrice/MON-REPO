package com.bizmaster.service.template.dto;

import jakarta.validation.constraints.NotBlank;

public class AcceptInvitationRequest {
    @NotBlank
    private String invitationToken;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;

    public AcceptInvitationRequest() {}

    public AcceptInvitationRequest(String invitationToken, String password, String confirmPassword) {
        this.invitationToken = invitationToken;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getInvitationToken() { return invitationToken; }
    public void setInvitationToken(String invitationToken) { this.invitationToken = invitationToken; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
