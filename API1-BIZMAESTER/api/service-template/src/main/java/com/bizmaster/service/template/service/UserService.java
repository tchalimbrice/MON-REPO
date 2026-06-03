package com.bizmaster.service.template.service;

import com.bizmaster.service.template.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long userId, UserDto userDto);
    void deleteUser(Long userId);
    UserDto getUserById(Long userId);
    UserDto getUserByUsername(String username);
    String getPasswordHashByUsername(String username);
    UserDto getUserByEmail(String email);
    List<UserDto> getUsersByCompanyId(Long companyId);
    List<UserDto> getUsersByDomain(String domain);
    List<UserDto> getActiveUsersByCompanyId(Long companyId);
    List<UserDto> searchUsersByEmail(String emailFragment);
    List<UserDto> searchUsersByUsername(String usernameFragment);
    UserDto changePassword(Long userId, String oldPassword, String newPassword);
    UserDto resetPassword(String email, String newPassword);
    UserDto updateLastLogin(Long userId);
    boolean validatePassword(String rawPassword, String encodedPassword);
}
