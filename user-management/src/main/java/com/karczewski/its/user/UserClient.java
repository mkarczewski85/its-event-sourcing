package com.karczewski.its.user;

import com.karczewski.its.user.dto.CreateUserRequestDto;
import com.karczewski.its.user.dto.PatchUserRequestDto;
import com.karczewski.its.user.dto.UserFilters;
import com.karczewski.its.user.dto.UserPasswordChangeRequestDto;
import com.karczewski.its.user.entity.Department;
import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.entity.UserCredentials;
import com.karczewski.its.user.entity.UserRole;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserClient {

    Page<UserAccount> getUserAccounts(UserFilters filters, int offset, int limit);

    Collection<UserAccount> getUserAccounts(UserFilters filters);

    Collection<Department> getAllUserDepartments();

    Optional<UserAccount> findByEmail(String email);

    UserAccount createUserAccount(CreateUserRequestDto reqDTO);

    UserAccount patchUserAccount(UUID uuid, PatchUserRequestDto reqDTO);

    UserAccount resetUserCredentials(UUID uuid);

    void changeUserPassword(UUID uuid, UserPasswordChangeRequestDto dto);

    UserAccount getUserAccount(UUID uuid);

    void deactivateUserAccount(UUID uuid);

    boolean existsByUUIDAndRole(UUID uuid, UserRole role);

    boolean isPasswordValid(UserCredentials userCredentials, String password);

}
