package com.karczewski.its.api.administration.mapper;

import com.karczewski.its.api.administration.dto.DepartmentDto;
import com.karczewski.its.api.administration.dto.UserAccountDto;
import com.karczewski.its.user.entity.Department;
import com.karczewski.its.user.entity.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class UserAccountMappingComponent {

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
