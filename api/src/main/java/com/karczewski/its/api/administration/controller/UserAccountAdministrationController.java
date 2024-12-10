package com.karczewski.its.api.administration.controller;

import com.karczewski.its.api.administration.dto.DepartmentDto;
import com.karczewski.its.api.administration.dto.UserAccountDto;
import com.karczewski.its.api.administration.mapper.UserAccountMappingComponent;
import com.karczewski.its.api.pagination.PageWrapper;
import com.karczewski.its.user.UserClient;
import com.karczewski.its.user.dto.CreateUserRequestDto;
import com.karczewski.its.user.dto.PatchUserRequestDto;
import com.karczewski.its.user.dto.UserFilters;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping(UserAccountAdministrationController.REST_API_BASE_PATH)
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
public class UserAccountAdministrationController {

    static final String REST_API_BASE_PATH = "${rest.prefix}${rest.secured.path}" + "/users";

    private final UserClient administrationClient;
    private final UserAccountMappingComponent mappingComponent;

    @GetMapping
    public PageWrapper<UserAccountDto> getPagedUserAccounts(@RequestParam(defaultValue = "0") @Min(0) final int offset,
                                                            @RequestParam(defaultValue = "20") @Min(20) final int limit,
                                                            @RequestBody(required = false) final UserFilters filters) {
        return PageWrapper.from(administrationClient.getUserAccounts(filters, offset, limit)
                .map(mappingComponent::toDto));
    }

    @GetMapping("/{uuid}")
    public UserAccountDto getUserAccount(@PathVariable("uuid") final UUID uuid) {
        return mappingComponent.toDto(administrationClient.getUserAccount(uuid));
    }

    @PostMapping
    public UserAccountDto createUserAccount(@RequestBody @Valid @NotNull final CreateUserRequestDto reqDTO) {
        return mappingComponent.toDto(administrationClient.createUserAccount(reqDTO));
    }

    @PatchMapping("/{uuid}")
    public UserAccountDto patchUserAccount(@PathVariable("uuid") final UUID uuid,
                                           @RequestBody final PatchUserRequestDto reqDTO) {
        return mappingComponent.toDto(administrationClient.patchUserAccount(uuid, reqDTO));
    }

    @PostMapping("/{uuid}/reset-password")
    public UserAccountDto resetUserAccountCredentials(@PathVariable("uuid") final UUID uuid) {
        return mappingComponent.toDto(administrationClient.resetUserCredentials(uuid));
    }

    @PostMapping("/{uuid}/deactivate")
    public void deactivateUserAccountCredentials(@PathVariable("uuid") final UUID uuid) {
        administrationClient.deactivateUserAccount(uuid);
    }

    @GetMapping("/departments")
    public Collection<DepartmentDto> getAllDepartments() {
        return administrationClient.getAllUserDepartments()
                .stream()
                .map(mappingComponent::toDto)
                .toList();
    }

}
