package com.karczewski.its.user.component;

import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.exception.UserAccountNotFoundException;
import com.karczewski.its.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeactivateUserComponent {

    private final UserAccountRepository userRepository;

    public void deactivateUser(final UUID uuid) {
        UserAccount userAccount = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserAccountNotFoundException("Unable to find user with id: " + uuid));
        userAccount.deactivate();
    }

}
