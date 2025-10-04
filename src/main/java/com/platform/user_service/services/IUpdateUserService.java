package com.platform.user_service.services;

import com.platform.user_service.dtos.request.UserUpdateDto;
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
}
