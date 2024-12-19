package com.karczewski.its.api.administration.mapper;

import com.karczewski.its.api.administration.dto.DepartmentDto;
import com.karczewski.its.api.administration.dto.UserAccountDto;
import com.karczewski.its.security.authentication.AuthenticationClient;
import com.karczewski.its.user.dto.UserFilters;
import com.karczewski.its.user.entity.Department;
import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserAccountMappingComponent {

    private final AuthenticationClient authenticationClient;

    public UserFilters toFilters(UUID id, String namePhrase, UUID department, String userRole, Boolean isActive) {
        return UserFilters.builder()
                .uuid(id)
                .namePhrase(namePhrase)
                .isActive(isActive)
                .department(department)
                .userRole(userRole)
                .build();
    }

    public UserFilters getAvailableTechniciansFilters() {
        return UserFilters.builder()
                .userRole(UserRole.TECHNICIAN.name())
                .excludedUuid(authenticationClient.getLoggedUserUuid())
                .isActive(true)
                .build();
    }

    public UserAccountDto toDto(UserAccount userAccount) {
        return UserAccountDto.builder()
                .id(userAccount.getId())
                .firstName(userAccount.getFirstName())
                .lastName(userAccount.getLastName())
                .role(userAccount.getRole().toString())
                .isActive(userAccount.isActive())
                .email(userAccount.getEmail())
                .department(toDto(userAccount.getDepartment()))
                .build();
    }

    public DepartmentDto toDto(Department department) {
        return DepartmentDto.builder()
                .uuid(department.getId())
                .name(department.getName())
                .location(department.getLocation())
                .build();
    }

}
