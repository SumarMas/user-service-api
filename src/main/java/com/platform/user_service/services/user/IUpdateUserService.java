package com.platform.user_service.services.user;

import com.platform.user_service.dtos.request.UserUpdateDto;
import com.platform.user_service.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.UUID;
/**
 * Service interface for updating user information.
 */
@Service
public interface IUpdateUserService {
    /**
     * Updates the user information based on the provided user ID and update data.
     *
     * @param userId        The UUID of the user to be updated.
     * @param userUpdateDto The DTO containing the updated user information.
     */
    void updateUser(UUID userId, UserUpdateDto userUpdateDto);

    /**
     * Adds a role to the user based on the provided user ID and role name.
     *
     * @param userId   The UUID of the user to whom the role will be added.
     * @param role The name of the role to be added to the user.
     */
    void addRoleToUser(UUID userId, UserRole role);
}
