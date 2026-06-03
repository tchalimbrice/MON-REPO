package com.bizmaster.service.template.controller;

import com.bizmaster.service.template.dto.ApiResponse;
import com.bizmaster.service.template.dto.ChangePasswordRequest;
import com.bizmaster.service.template.dto.LoginRequest;
import com.bizmaster.service.template.dto.LoginResponse;
import com.bizmaster.service.template.dto.RegisterRequest;
import com.bizmaster.service.template.dto.UserDto;
import com.bizmaster.service.template.service.UserService;
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
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody RegisterRequest req) {
        UserDto dto = new UserDto();
        dto.setUsername(req.getUsername());
        dto.setEmail(req.getEmail());
        dto.setFirstName(req.getFirstName());
        dto.setLastName(req.getLastName());
        dto.setDomain(req.getDomain());
        dto.setPassword(req.getPassword());

        UserDto created = userService.createUser(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "User created", created));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest req) {
        // Authentication should be delegated to auth-server; placeholder behaviour
        UserDto user = userService.getUserByUsername(req.getUsername());
        String encodedPassword = userService.getPasswordHashByUsername(req.getUsername());
        if (!userService.validatePassword(req.getPassword(), encodedPassword)) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid credentials"));
        }
        LoginResponse resp = new LoginResponse();
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setEmail(user.getEmail());
        resp.setDomain(user.getDomain());
        resp.setRole(user.getRole());
        resp.setToken("");
        resp.setRefreshToken("");
        return ResponseEntity.ok(new ApiResponse<>(true, "Authenticated", resp));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User found", user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        UserDto updated = userService.updateUser(id, userDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "User updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted"));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<UserDto>> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest req) {
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Passwords do not match"));
        }
        UserDto updated = userService.changePassword(id, req.getOldPassword(), req.getNewPassword());
        return ResponseEntity.ok(new ApiResponse<>(true, "Password changed", updated));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> listUsers(
        @RequestParam(required = false) Long companyId,
        @RequestParam(required = false) String domain,
        @RequestParam(required = false) Boolean active
    ) {
        List<UserDto> users;
        if (companyId != null && active != null) {
            users = active ? userService.getActiveUsersByCompanyId(companyId) : userService.getUsersByCompanyId(companyId);
        } else if (companyId != null) {
            users = userService.getUsersByCompanyId(companyId);
        } else if (domain != null) {
            users = userService.getUsersByDomain(domain);
        } else {
            users = List.of();
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched", users));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserDto>>> searchUsers(
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String username
    ) {
        List<UserDto> users;
        if (email != null && !email.isBlank()) {
            users = userService.searchUsersByEmail(email);
        } else if (username != null && !username.isBlank()) {
            users = userService.searchUsersByUsername(username);
        } else {
            users = List.of();
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Users searched", users));
    }
}
