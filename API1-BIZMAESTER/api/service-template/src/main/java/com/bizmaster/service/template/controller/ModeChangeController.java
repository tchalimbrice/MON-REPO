package com.bizmaster.service.template.controller;

import com.bizmaster.service.template.dto.ApiResponse;
import com.bizmaster.service.template.dto.ModeChangeHistoryDto;
import com.bizmaster.service.template.service.ModeChangeService;
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
@RequestMapping("/api/mode-change")
@Validated
public class ModeChangeController {

    private final ModeChangeService modeChangeService;

    public ModeChangeController(ModeChangeService modeChangeService) {
        this.modeChangeService = modeChangeService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<ModeChangeHistoryDto>> initiate(@RequestParam Long companyId,
                                                                      @RequestParam String newMode,
                                                                      @RequestParam Long userId) {
        ModeChangeHistoryDto dto = modeChangeService.initiateModeSwitching(companyId, newMode, userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mode change initiated", dto));
    }

    @PostMapping("/{id}/update-status")
    public ResponseEntity<ApiResponse<ModeChangeHistoryDto>> updateStatus(@PathVariable Long id, @RequestParam String status) {
        ModeChangeHistoryDto dto = modeChangeService.updateModeChangeStatus(id, status);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mode change updated", dto));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<ModeChangeHistoryDto>> complete(@PathVariable Long id) {
        ModeChangeHistoryDto dto = modeChangeService.completeModeChange(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mode change completed", dto));
    }

    @PostMapping("/{id}/fail")
    public ResponseEntity<ApiResponse<ModeChangeHistoryDto>> fail(@PathVariable Long id, @RequestParam String message) {
        ModeChangeHistoryDto dto = modeChangeService.failModeChange(id, message);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mode change failed", dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ModeChangeHistoryDto>>> history(@RequestParam Long companyId) {
        List<ModeChangeHistoryDto> list = modeChangeService.getModeChangeHistoryByCompany(companyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mode change history fetched", list));
    }
}
