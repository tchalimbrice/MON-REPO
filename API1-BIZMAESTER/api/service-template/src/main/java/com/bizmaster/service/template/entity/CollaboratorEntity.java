package com.bizmaster.service.template.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "collaborators")
public class CollaboratorEntity {

    public enum CollaboratorStatus {
        PENDING,      // En attente d'acceptation
        ACTIVE,       // Actif
        SUSPENDED,    // Suspendu
        REMOVED       // Retiré
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String position;  // Poste/Titre

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollaboratorStatus status = CollaboratorStatus.ACTIVE;

    @Column(nullable = false)
    private boolean dataAccess = true;  // Accès aux données de l'entreprise

    @Column(nullable = false)
    private boolean reportAccess = true;  // Accès aux rapports

    @Column(nullable = false)
    private boolean settingsAccess = false;  // Accès aux paramètres (seulement CEO)

    private String notes;
    private Instant dateJoined;
    private Instant dateLeft;
    private Instant createdAt;
    private Instant updatedAt;

    // Constructors
    public CollaboratorEntity() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public CollaboratorEntity(CompanyEntity company, UserEntity user, String position) {
        this.company = company;
        this.user = user;
        this.position = position;
        this.status = CollaboratorStatus.ACTIVE;
        this.dateJoined = Instant.now();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public CollaboratorStatus getStatus() {
        return status;
    }

    public void setStatus(CollaboratorStatus status) {
        this.status = status;
    }

    public boolean isDataAccess() {
        return dataAccess;
    }

    public void setDataAccess(boolean dataAccess) {
        this.dataAccess = dataAccess;
    }

    public boolean isReportAccess() {
        return reportAccess;
    }

    public void setReportAccess(boolean reportAccess) {
        this.reportAccess = reportAccess;
    }

    public boolean isSettingsAccess() {
        return settingsAccess;
    }

    public void setSettingsAccess(boolean settingsAccess) {
        this.settingsAccess = settingsAccess;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Instant dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Instant getDateLeft() {
        return dateLeft;
    }

    public void setDateLeft(Instant dateLeft) {
        this.dateLeft = dateLeft;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
