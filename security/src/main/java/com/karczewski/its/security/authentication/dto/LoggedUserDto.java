package com.karczewski.its.security.authentication.dto;


import lombok.Builder;

import java.util.UUID;

@Builder
public record LoggedUserDto(UUID id,
                            String username,
                            String firstName,
                            String lastName,
                            String role,
                            String department,
                            String location
) {
}
