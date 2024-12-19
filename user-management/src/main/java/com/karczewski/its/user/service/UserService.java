package com.karczewski.its.user.service;

import com.karczewski.its.user.UserClient;
import com.karczewski.its.user.component.*;
import com.karczewski.its.user.dto.*;
import com.karczewski.its.user.entity.*;
import com.karczewski.its.user.notification.EmailNotificationService;
import com.karczewski.its.user.repository.DepartmentRepository;
import com.karczewski.its.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
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
    private final UserPasswordChangeComponent userPasswordChangeComponent;
    private final UserAccountPasswordResetComponent userPasswordResetComponent;
    private final EmailNotificationService emailNotificationService;
    private final UserSettingsPatchComponent userSettingsPatchComponent;

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
        UserAccount user = userCreateComponent.createUser(dto);
        UserCredentials userCredentials = user.getUserCredentials();
        emailNotificationService.sendWelcomeEmail(user.getEmail(), userCredentials.getRawPassword());
        return user;
    }

    @Override
    @Transactional
    public UserAccount patchUserAccount(UUID uuid, PatchUserRequestDto dto) {
        return patchComponent.patchUser(uuid, dto);
    }

    @Override
    @Transactional
    public void patchUserSettings(UUID uuid, PatchUserSettingsRequestDto dto) {
        userSettingsPatchComponent.patchUserSettings(uuid, dto);
    }

    @Override
    @Transactional
    public UserAccount resetUserCredentials(UUID uuid) {
        UserAccount user = queryComponent.getByUUID(uuid);
        credentialsCreateComponent.recreateUserCredentials(user);
        return user;
    }

    @Override
    @Transactional
    public void changeUserPassword(UUID uuid, UserPasswordChangeRequestDto dto) {
        userPasswordChangeComponent.updateUserCredentials(uuid, dto);
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
    @Async
    public void sendPasswordResetToken(String email) {
        Optional<UserAccount> userOpt = userAccountRepository.findByEmailAndIsActive(email, true);
        if (userOpt.isEmpty()) return;
        PasswordResetToken resetToken = userPasswordResetComponent.generatePasswordResetToken(userOpt.get());
        emailNotificationService.sendPasswordResetEmail(email, resetToken.getToken());
    }

    @Override
    public void resetUserPasswordByToken(ResetPasswordByTokenRequestDto dto) {
        userPasswordResetComponent.setNewPasswordByResetToken(dto);
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
