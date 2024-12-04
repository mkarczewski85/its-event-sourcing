package com.karczewski.its.user.service;

import com.karczewski.its.user.UserClient;
import com.karczewski.its.user.dto.CreateUserRequestDto;
import com.karczewski.its.user.dto.PatchUserRequestDto;
import com.karczewski.its.user.dto.UserFilters;
import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.entity.UserCredentials;
import com.karczewski.its.user.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserClient {
    @Override
    public Page<UserAccount> getUsers(UserFilters filters, int offset, int limit) {
        return null;
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public UserAccount createUser(CreateUserRequestDto reqDTO) {
        return null;
    }

    @Override
    public UserAccount patchUser(UUID uuid, PatchUserRequestDto reqDTO) {
        return null;
    }

    @Override
    public UserAccount resetUserCredentials(UUID uuid) {
        return null;
    }

    @Override
    public UserAccount getByUUID(UUID uuid) {
        return null;
    }

    @Override
    public UserAccount getRandomWith(UserRole role) {
        return null;
    }

    @Override
    public void deactivateUser(UUID uuid) {

    }

    @Override
    public boolean isPasswordValid(UserCredentials userCredentials, String password) {
        return false;
    }
}
