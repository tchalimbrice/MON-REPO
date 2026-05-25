package com.bizmaster.auth.service;

import com.bizmaster.auth.dto.AuthResponseDto;
import com.bizmaster.auth.dto.LoginRequestDto;
import com.bizmaster.auth.dto.RegisterRequestDto;

public interface UserService {
    AuthResponseDto authenticate(LoginRequestDto request);
    AuthResponseDto register(RegisterRequestDto request);
}
