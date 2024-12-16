package com.karczewski.its.security.authentication.component;

import com.karczewski.its.security.authentication.dto.JWTokenDto;
import com.karczewski.its.security.authentication.dto.LoggedUserDto;
import com.karczewski.its.security.authentication.dto.UserLoginPasswordDto;
import com.karczewski.its.security.authentication.model.UserAuthentication;
import com.karczewski.its.user.UserClient;
import com.karczewski.its.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAuthenticationComponent {

    private static final String ROLE_PREFIX = "ROLE_";

    private final UserClient userClient;
    private final JWTUtilityComponent jwtUtility;

    public JWTokenDto authenticateUser(final UserLoginPasswordDto credentials) {
        final String login = credentials.login();
        final String password = credentials.password();
        final UserAccount user = userClient.findByEmail(login)
                .orElseThrow(() -> new BadCredentialsException("Error for user: " + login));
        if (user.isActive() && userClient.isPasswordValid(user.getUserCredentials(), password)) {
            UserAuthentication authentication = createAuthentication(user, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String jwToken = jwtUtility.createJWT(authentication);
            final LoggedUserDto userDto = toLoggedUserDTO(user);
            return JWTokenDto.builder()
                    .token(jwToken)
                    .expiresIn(jwtUtility.getTokenValidityInSeconds())
                    .userInfo(userDto)
                    .build();
        } else {
            throw new BadCredentialsException("Error for user: " + login);
        }
    }

    private UserAuthentication createAuthentication(final UserAccount user, final String password) {
        final UserAuthentication authenticationToken = new UserAuthentication(user.getEmail(),
                password, List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(user.getRole().name()))));
        authenticationToken.setId(user.getId().toString());
        authenticationToken.setFirstName(user.getFirstName());
        authenticationToken.setLastName(user.getLastName());
        authenticationToken.setDepartment(user.getDepartment().getName());
        return authenticationToken;
    }

    private static LoggedUserDto toLoggedUserDTO(final UserAccount user) {
        return LoggedUserDto.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .department(user.getDepartment().getName())
                .location(user.getDepartment().getLocation())
                .build();
    }

}
