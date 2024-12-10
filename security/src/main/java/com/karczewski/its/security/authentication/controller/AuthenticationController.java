package com.karczewski.its.security.authentication.controller;

import com.karczewski.its.security.authentication.AuthenticationClient;
import com.karczewski.its.security.authentication.dto.JWTokenDto;
import com.karczewski.its.security.authentication.dto.LoggedUserDto;
import com.karczewski.its.security.authentication.dto.UserLoginPasswordDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(AuthenticationController.REST_API_BASE_PATH)
@RequiredArgsConstructor
public class AuthenticationController {

    static final String REST_API_BASE_PATH = "${rest.prefix}";
    private static final String AUTHENTICATE_PATH = "${rest.public.path}/authenticate";
    private static final String ME_PATH = "${rest.secured.path}/me";

    private final AuthenticationClient authenticationClient;

    @PostMapping(AUTHENTICATE_PATH)
    public ResponseEntity<JWTokenDto> authenticate(@RequestBody @NotNull @Valid final UserLoginPasswordDto loginPasswordDto) {
        final JWTokenDto jwToken = authenticationClient.authenticateUser(loginPasswordDto);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwToken.token());
        return new ResponseEntity<>(jwToken, httpHeaders, HttpStatus.OK);
    }

    @GetMapping(ME_PATH)
    public LoggedUserDto getLoggedUser() {
        return authenticationClient.getLoggedUserDetails();
    }

}
