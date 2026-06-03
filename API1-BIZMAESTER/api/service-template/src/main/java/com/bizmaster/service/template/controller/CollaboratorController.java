package com.bizmaster.service.template.controller;

import com.bizmaster.service.template.dto.ApiResponse;
import com.bizmaster.service.template.dto.CollaboratorDto;
import com.bizmaster.service.template.service.CollaboratorService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/collaborators")
@Validated
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    public CollaboratorController(CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CollaboratorDto>> add(@Valid @RequestBody CollaboratorDto dto) {
        CollaboratorDto created = collaboratorService.addCollaborator(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Collaborator added", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CollaboratorDto>> update(@PathVariable Long id, @Valid @RequestBody CollaboratorDto dto) {
        CollaboratorDto updated = collaboratorService.updateCollaborator(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Collaborator updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Long id) {
        collaboratorService.removeCollaborator(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Collaborator removed"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CollaboratorDto>> getById(@PathVariable Long id) {
        CollaboratorDto dto = collaboratorService.getCollaboratorById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Collaborator fetched", dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CollaboratorDto>>> listByCompany(@RequestParam Long companyId) {
        List<CollaboratorDto> list = collaboratorService.getCollaboratorsByCompanyId(companyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Collaborators fetched", list));
    }

    @PostMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<Void>> suspend(@PathVariable Long id) {
        collaboratorService.suspendCollaborator(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Collaborator suspended"));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id) {
        collaboratorService.activateCollaborator(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Collaborator activated"));
    }
}
