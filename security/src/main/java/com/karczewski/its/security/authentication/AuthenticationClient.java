package com.karczewski.its.security.authentication;

import com.karczewski.its.security.authentication.dto.JWTokenDto;
import com.karczewski.its.security.authentication.dto.LoggedUserDto;
import com.karczewski.its.security.authentication.dto.UserLoginPasswordDto;

import java.util.UUID;

public interface AuthenticationClient {

    JWTokenDto authenticateUser(UserLoginPasswordDto credentials);

    LoggedUserDto getLoggedUserDetails();

    UUID getLoggedUserUuid();

}
