package com.karczewski.its.api.utility;

import com.karczewski.its.api.administration.dto.UserAccountDto;
import com.karczewski.its.api.administration.mapper.UserAccountMappingComponent;
import com.karczewski.its.api.pagination.PageWrapper;
import com.karczewski.its.user.UserClient;
import com.karczewski.its.user.dto.UserFilters;
import com.karczewski.its.user.entity.UserRole;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping(UtilityController.BASE_PATH)
@RequiredArgsConstructor
public class UtilityController {

    static final String BASE_PATH = "${rest.prefix}${rest.secured.path}";
    private static final UserFilters TECHNICIAN_USER_FILTER = UserFilters.builder()
            .userRole(UserRole.TECHNICIAN.name())
            .isActive(true)
            .build();

    private final UserClient userClient;
    private final UserAccountMappingComponent mappingComponent;

    @GetMapping("/users/technicians")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public PageWrapper<UserAccountDto> getAvailableTechnicians(@RequestParam(defaultValue = "0") @Min(0) final int offset,
                                                               @RequestParam(defaultValue = "20") @Min(20) final int limit) {
        return PageWrapper.from((userClient.getUserAccounts(TECHNICIAN_USER_FILTER, offset, limit)
                .map(mappingComponent::toDto)));
    }

}
