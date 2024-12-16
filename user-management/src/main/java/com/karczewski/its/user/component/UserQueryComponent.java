package com.karczewski.its.user.component;

import com.karczewski.its.user.dto.UserFilters;
import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.exception.UserAccountNotFoundException;
import com.karczewski.its.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserQueryComponent {

    private final UserAccountRepository userRepository;
    private final UserSpecificationComponent specificationComponent;

    public Page<UserAccount> getUsers(final UserFilters filters, final int offset, final int limit) {
        int pageNo = (limit + offset) / limit;
        final PageRequest pageRequest = PageRequest.of(--pageNo, limit, Sort.by(Sort.Direction.ASC, "lastName"));
        final Specification<UserAccount> specification = specificationComponent.getUserSpecification(filters);
        return userRepository.findAll(specification, pageRequest);
    }

    public Collection<UserAccount> getUsers(final UserFilters filters) {
        final Specification<UserAccount> specification = specificationComponent.getUserSpecification(filters);
        return userRepository.findAll(specification);
    }

    public Optional<UserAccount> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public UserAccount getByUUID(final UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new UserAccountNotFoundException("Unable to find user"));
    }

}
