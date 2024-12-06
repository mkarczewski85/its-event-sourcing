package com.karczewski.its.user;

import com.karczewski.its.user.dto.CreateUserRequestDto;
import com.karczewski.its.user.dto.PatchUserRequestDto;
import com.karczewski.its.user.dto.UserFilters;
import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.entity.UserCredentials;
import com.karczewski.its.user.entity.UserRole;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface UserClient {

    Page<UserAccount> getUsers(UserFilters filters, int offset, int limit);

    Optional<UserAccount> findByEmail(String email);

    UserAccount createUser(CreateUserRequestDto reqDTO);

    UserAccount patchUser(UUID uuid, PatchUserRequestDto reqDTO);

    UserAccount resetUserCredentials(UUID uuid);

    UserAccount getByUUID(UUID uuid);

    UserAccount getRandomWith(UserRole role);

    void deactivateUser(UUID uuid);

    boolean existsByUUIDAndRole(UUID uuid, UserRole role);

    boolean isPasswordValid(UserCredentials userCredentials, String password);

}
