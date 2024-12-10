package com.karczewski.its.user.component;

import com.karczewski.its.user.dto.CreateUserRequestDto;
import com.karczewski.its.user.entity.Department;
import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.entity.UserCredentials;
import com.karczewski.its.user.entity.UserRole;
import com.karczewski.its.user.exception.UserAccountAlreadyExistsException;
import com.karczewski.its.user.exception.UserAccountNotFoundException;
import com.karczewski.its.user.repository.DepartmentRepository;
import com.karczewski.its.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserCreateComponent {

    private final UserAccountRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserCredentialsCreateComponent credentialsCreateComponent;

    public UserAccount createUser(CreateUserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAccountAlreadyExistsException("User with given email address already exists");
        }
        Department department = findDepartment(dto.getDepartmentId());
        UserAccount user = createUser(dto, department);
        UserCredentials userCredentials = credentialsCreateComponent.createUserCredentials(user);
        user.setUserCredentials(userCredentials);
        return userRepository.save(user);
    }

    private static UserAccount createUser(CreateUserRequestDto dto,
                                          Department department) {
        return UserAccount.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .role(UserRole.valueOf(dto.getRole()))
                .email(dto.getEmail())
                .department(department)
                .build();
    }

    private Department findDepartment(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new UserAccountNotFoundException("Unable to find department"));
    }

}
