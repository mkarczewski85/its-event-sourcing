package com.karczewski.its.security.authentication.service;

import com.karczewski.its.security.authentication.AuthenticationClient;
import com.karczewski.its.security.authentication.component.AuthenticatedUserInfoComponent;
import com.karczewski.its.security.authentication.component.UserAuthenticationComponent;
import com.karczewski.its.security.authentication.dto.JWTokenDto;
import com.karczewski.its.security.authentication.dto.LoggedUserDto;
import com.karczewski.its.security.authentication.dto.UserLoginPasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationClient {

    private final AuthenticatedUserInfoComponent authenticatedUserInfoComponent;
    private final UserAuthenticationComponent userAuthenticationComponent;

    @Override
    public JWTokenDto authenticateUser(final UserLoginPasswordDto credentials) {
        return userAuthenticationComponent.authenticateUser(credentials);
    }

    @Override
    public LoggedUserDto getLoggedUserDetails() {
        return authenticatedUserInfoComponent.getLoggedUserDetails();
    }

    @Override
    public UUID getLoggedUserUuid() {
        return this.getLoggedUserDetails().id();
    }

}
