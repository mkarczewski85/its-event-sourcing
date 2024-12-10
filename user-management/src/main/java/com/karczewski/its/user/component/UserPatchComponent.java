package com.karczewski.its.user.component;

import com.karczewski.its.user.dto.PatchUserRequestDto;
import com.karczewski.its.user.entity.Department;
import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPatchComponent {

    private final UserQueryComponent queryComponent;
    private final DepartmentRepository departmentRepository;

    public UserAccount patchUser(final UUID uuid, final PatchUserRequestDto reqDTO) {
        final UserAccount user = queryComponent.getByUUID(uuid);
        return patchUser(user, reqDTO);
    }

    private UserAccount patchUser(final UserAccount user, final PatchUserRequestDto reqDTO) {
        updateFirstName(user, reqDTO);
        updateLastName(user, reqDTO);
        updateEmail(user, reqDTO);
        updateDepartment(user, reqDTO);
        updateAccountStatus(user, reqDTO);
        return user;
    }

    private static void updateFirstName(final UserAccount user, final PatchUserRequestDto reqDTO) {
        if (reqDTO.getFirstName() == null) return;
        user.setFirstName(reqDTO.getFirstName());
    }

    private static void updateLastName(final UserAccount user, final PatchUserRequestDto reqDTO) {
        if (reqDTO.getLastName() == null) return;
        user.setLastName(reqDTO.getLastName());
    }

    private static void updateEmail(final UserAccount user, final PatchUserRequestDto reqDTO) {
        if (reqDTO.getEmail() == null) return;
        user.setEmail(reqDTO.getEmail());
    }

    private void updateDepartment(final UserAccount user, final PatchUserRequestDto reqDTO) {
        if (reqDTO.getDepartmentId() == null) return;
        final Department department = departmentRepository.findById(reqDTO.getDepartmentId()).orElseThrow();
        user.setDepartment(department);
    }

    private static void updateAccountStatus(final UserAccount user, final PatchUserRequestDto reqDTO) {
        if (reqDTO.getIsActive() == null) return;
        user.setActive(reqDTO.getIsActive());
    }

}
