package com.karczewski.its.api.utility;

import com.karczewski.its.api.administration.dto.UserAccountDto;
import com.karczewski.its.api.administration.mapper.UserAccountMappingComponent;
import com.karczewski.its.user.UserClient;
import com.karczewski.its.user.dto.UserFilters;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@Validated
@RequestMapping(UtilityController.BASE_PATH)
@RequiredArgsConstructor
public class UtilityController {

    static final String BASE_PATH = "${rest.prefix}${rest.secured.path}";

    private final UserClient userClient;
    private final UserAccountMappingComponent mappingComponent;

    @GetMapping("/users/technicians")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public Collection<UserAccountDto> getAvailableTechnicians() {
        UserFilters availableTechniciansFilters = mappingComponent.getAvailableTechniciansFilters();
        return userClient.getUserAccounts(availableTechniciansFilters)
                .stream()
                .map(mappingComponent::toDto)
                .toList();
    }

}
