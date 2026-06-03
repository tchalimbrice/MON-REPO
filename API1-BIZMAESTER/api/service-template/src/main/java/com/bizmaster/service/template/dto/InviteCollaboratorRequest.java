package com.bizmaster.service.template.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class InviteCollaboratorRequest {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String invitedEmail;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Position is required")
    private String position;

    private boolean dataAccess = true;
    private boolean reportAccess = true;
    private boolean settingsAccess = false;

    public InviteCollaboratorRequest() {}

    public InviteCollaboratorRequest(String invitedEmail, String firstName, String lastName, String position) {
        this.invitedEmail = invitedEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
    }

    public String getInvitedEmail() { return invitedEmail; }
    public void setInvitedEmail(String invitedEmail) { this.invitedEmail = invitedEmail; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public boolean isDataAccess() { return dataAccess; }
    public void setDataAccess(boolean dataAccess) { this.dataAccess = dataAccess; }

    public boolean isReportAccess() { return reportAccess; }
    public void setReportAccess(boolean reportAccess) { this.reportAccess = reportAccess; }

    public boolean isSettingsAccess() { return settingsAccess; }
    public void setSettingsAccess(boolean settingsAccess) { this.settingsAccess = settingsAccess; }
}
