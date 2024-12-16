package com.karczewski.its.user.service;

import com.karczewski.its.user.UserClient;
import com.karczewski.its.user.component.*;
import com.karczewski.its.user.dto.CreateUserRequestDto;
import com.karczewski.its.user.dto.PatchUserRequestDto;
import com.karczewski.its.user.dto.UserFilters;
import com.karczewski.its.user.entity.Department;
import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.entity.UserCredentials;
import com.karczewski.its.user.entity.UserRole;
import com.karczewski.its.user.repository.DepartmentRepository;
import com.karczewski.its.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserClient {

    private final UserAccountRepository userAccountRepository;
    private final DepartmentRepository departmentRepository;
    private final UserQueryComponent queryComponent;
    private final UserPasswordValidateComponent passwordValidateComponent;
    private final UserCreateComponent userCreateComponent;
    private final UserCredentialsCreateComponent credentialsCreateComponent;
    private final UserPatchComponent patchComponent;
    private final DeactivateUserComponent deactivateComponent;

    @Override
    @Transactional(readOnly = true)
    public Page<UserAccount> getUserAccounts(UserFilters filters, int offset, int limit) {
        return queryComponent.getUsers(filters, offset, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserAccount> getUserAccounts(UserFilters filters) {
        return queryComponent.getUsers(filters);
    }

    @Override
    public Collection<Department> getAllUserDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAccount> findByEmail(String email) {
        return queryComponent.findByEmail(email);
    }

    @Override
    @Transactional
    public UserAccount createUserAccount(CreateUserRequestDto dto) {
        return userCreateComponent.createUser(dto);
    }

    @Override
    @Transactional
    public UserAccount patchUserAccount(UUID uuid, PatchUserRequestDto dto) {
        return patchComponent.patchUser(uuid, dto);
    }

    @Override
    @Transactional
    public UserAccount resetUserCredentials(UUID uuid) {
        UserAccount user = queryComponent.getByUUID(uuid);
        credentialsCreateComponent.recreateUserCredentials(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccount getUserAccount(UUID uuid) {
        return queryComponent.getByUUID(uuid);
    }

    @Override
    @Transactional
    public void deactivateUserAccount(UUID uuid) {
        deactivateComponent.deactivateUser(uuid);
    }

    @Override
    public boolean isPasswordValid(UserCredentials userCredentials, String password) {
        return passwordValidateComponent.isPasswordValid(userCredentials, password);
    }

    @Override
    public boolean existsByUUIDAndRole(UUID uuid, UserRole role) {
        return userAccountRepository.existsByIdAndRole(uuid, role); // TODO
    }
}
