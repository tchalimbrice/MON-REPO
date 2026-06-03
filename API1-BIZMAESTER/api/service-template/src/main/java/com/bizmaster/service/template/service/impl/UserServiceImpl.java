package com.bizmaster.service.template.service.impl;

import com.bizmaster.service.template.dto.UserDto;
import com.bizmaster.service.template.entity.UserEntity;
import com.bizmaster.service.template.mapper.UserMapper;
import com.bizmaster.service.template.repository.UserRepository;
import com.bizmaster.service.template.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        // Check if user already exists
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
        }
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }

        UserEntity entity = new UserEntity(
            userDto.getUsername(),
            passwordEncoder.encode(userDto.getPassword()),
            userDto.getEmail(),
            userDto.getFirstName(),
            userDto.getLastName(),
            userDto.getDomain()
        );

        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        
        UserEntity saved = userRepository.save(entity);
        return UserMapper.toDto(saved);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        entity.setFirstName(userDto.getFirstName());
        entity.setLastName(userDto.getLastName());
        entity.setActive(userDto.isActive());
        entity.setUpdatedAt(Instant.now());

        UserEntity saved = userRepository.save(entity);
        return UserMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        userRepository.delete(entity);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId)
            .map(UserMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(UserMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    @Override
    public String getPasswordHashByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(UserEntity::getPassword)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(UserMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }

    @Override
    public List<UserDto> getUsersByCompanyId(Long companyId) {
        return userRepository.findByCompanyId(companyId).stream()
            .map(UserMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsersByDomain(String domain) {
        return userRepository.findByDomain(domain).stream()
            .map(UserMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getActiveUsersByCompanyId(Long companyId) {
        return userRepository.findByCompanyIdAndActive(companyId, true).stream()
            .map(UserMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> searchUsersByEmail(String emailFragment) {
        return userRepository.findByEmailContainingIgnoreCase(emailFragment).stream()
            .map(UserMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> searchUsersByUsername(String usernameFragment) {
        return userRepository.findByUsernameContainingIgnoreCase(usernameFragment).stream()
            .map(UserMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto changePassword(Long userId, String oldPassword, String newPassword) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (!validatePassword(oldPassword, entity.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }

        entity.setPassword(passwordEncoder.encode(newPassword));
        entity.setPasswordChangeRequired(false);
        entity.setUpdatedAt(Instant.now());

        UserEntity saved = userRepository.save(entity);
        return UserMapper.toDto(saved);
    }

    @Override
    @Transactional
    public UserDto resetPassword(String email, String newPassword) {
        UserEntity entity = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        entity.setPassword(passwordEncoder.encode(newPassword));
        entity.setPasswordChangeRequired(true);
        entity.setUpdatedAt(Instant.now());

        UserEntity saved = userRepository.save(entity);
        return UserMapper.toDto(saved);
    }

    @Override
    @Transactional
    public UserDto updateLastLogin(Long userId) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        entity.setLastLogin(Instant.now());
        entity.setUpdatedAt(Instant.now());

        UserEntity saved = userRepository.save(entity);
        return UserMapper.toDto(saved);
    }

    @Override
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
