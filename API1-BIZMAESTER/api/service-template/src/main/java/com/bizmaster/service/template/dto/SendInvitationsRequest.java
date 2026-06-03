package com.bizmaster.service.template.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class SendInvitationsRequest {

    @Valid
    @NotNull(message = "Owner information is required")
    private SendInvitationOwner owner;

    @Valid
    @NotEmpty(message = "At least one invitation is required")
    private List<SendInvitationItem> invitations;

    public SendInvitationOwner getOwner() {
        return owner;
    }

    public void setOwner(SendInvitationOwner owner) {
        this.owner = owner;
    }

    public List<SendInvitationItem> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<SendInvitationItem> invitations) {
        this.invitations = invitations;
    }

    public static class SendInvitationOwner {

        @NotBlank(message = "Owner email is required")
        @Email(message = "Owner email must be valid")
        private String email;

        @NotBlank(message = "Owner name is required")
        private String name;

        @NotBlank(message = "Owner url is required")
        private String url;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class SendInvitationItem {

        @NotBlank(message = "Invitation name is required")
        private String name;

        @NotBlank(message = "Invitation email is required")
        @Email(message = "Invitation email must be valid")
        private String email;

        @NotBlank(message = "Invitation url is required")
        private String url;

        private String role;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
