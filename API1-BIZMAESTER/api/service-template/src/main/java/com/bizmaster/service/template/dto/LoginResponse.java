package com.bizmaster.service.template.dto;

public class LoginResponse {
    private String token;
    private String refreshToken;
    private Long userId;
    private String username;
    private String email;
    private String role;
    private String domain;

    public LoginResponse() {}

    public LoginResponse(String token, String refreshToken, Long userId, String username, String email, String role, String domain) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.domain = domain;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
}
