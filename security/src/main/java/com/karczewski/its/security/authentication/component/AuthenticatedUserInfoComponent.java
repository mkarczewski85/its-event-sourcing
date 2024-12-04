package com.karczewski.its.security.authentication.component;

import com.karczewski.its.security.authentication.dto.LoggedUserDto;
import com.karczewski.its.security.authentication.model.UserAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticatedUserInfoComponent {

    private static final String EMPTY = "";
    private static final String ROLE_PREFIX = "ROLE_";

    public LoggedUserDto getLoggedUserDetails() {
        final UserAuthentication authentication = (UserAuthentication) getUserDetailsFromContext();
        return LoggedUserDto.builder()
                .id(UUID.fromString(authentication.getId()))
                .username(getUsername(authentication))
                .firstName(authentication.getFirstName())
                .lastName(authentication.getLastName())
                .role(getRole(authentication))
                .department(authentication.getDepartment())
                .location(authentication.getLocation())
                .build();
    }

    private String getUsername(UserAuthentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return (String) authentication.getPrincipal();
    }

    private Authentication getUserDetailsFromContext() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private String getRole(final Authentication authentication) {
        return authentication.getAuthorities().stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().startsWith(ROLE_PREFIX))
                .map(grantedAuthority -> grantedAuthority.getAuthority()
                        .replace(ROLE_PREFIX, EMPTY)).findFirst().orElseThrow();
    }

}
