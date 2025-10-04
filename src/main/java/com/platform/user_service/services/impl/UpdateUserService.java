package com.platform.user_service.services.impl;

import com.platform.user_service.context.RequestContext;
import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.request.UserUpdateDto;
import com.platform.user_service.entities.UserEntity;
import com.platform.user_service.repositories.UserRepository;
import com.platform.user_service.services.IContextService;
import com.platform.user_service.services.IUpdateUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
/**
 * Implementation of the IUpdateUserService interface for updating user information.
 */
@Service
@AllArgsConstructor
public class UpdateUserService implements IUpdateUserService {
    /** Logger instance for logging information and errors. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserService.class);
    /** Repository for accessing user data. */
    private final UserRepository userRepository;
    /** Service for context-related operations. */
    private final IContextService contextService;

    /**
     * Updates the user information based on the provided user ID and update data.
     *
     * @param userId        The UUID of the user to be updated.
     * @param userUpdateDto The DTO containing the updated user information.
     */
    @Override
    public void updateUser(final UUID userId, final UserUpdateDto userUpdateDto) {
        LOGGER.trace("updateUser()");
        LOGGER.info("Updating user with ID: {}", userId);
        validateUserIdWithContext(userId);
        UserEntity userEntity = getUserEntity(userId);

        // Update user fields based on the provided DTO
        if (userUpdateDto.getFirstName() != null) {
            userEntity.setFirstName(userUpdateDto.getFirstName());
        }
        if (userUpdateDto.getLastName() != null) {
            userEntity.setLastName(userUpdateDto.getLastName());
        }
        if (userUpdateDto.getProfileFileId() != null) {
            userEntity.setProfileFileId(parseProfileFileId(userUpdateDto.getProfileFileId()));
        }
        UUID userUpdatedBy = getUserIdFromContext();
        userEntity.setLastUpdatedUser(userUpdatedBy);
        try {
            userRepository.save(userEntity);
            LOGGER.info("User with ID: {} successfully updated.", userId);
        } catch (DataAccessException ex) {
            LOGGER.error("Database access error while updating user ID: {}, ERROR: {}",
                    userId, ex.getMessage());
            throw new CustomException("Error updating user data", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    private UserEntity getUserEntity(UUID id) {
        LOGGER.trace("Retrieving user entity by ID: {}", id);
        try {
            Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
            if (optionalUserEntity.isEmpty()) {
                LOGGER.info("User with ID '{}' not found or is disabled.", id.toString());
                throw new CustomException("User not found or is disabled", HttpStatus.NOT_FOUND);
            }
            LOGGER.trace("User with ID '{}' found.", id.toString());
            return optionalUserEntity.get();
        } catch (DataAccessException ex) {
            LOGGER.error("Database access error while retrieving user by ID: {}, ERROR: {}",
                    id.toString(), ex.getMessage());
            throw new CustomException("Error retrieving user data", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    private UUID parseProfileFileId(String profileFileId) {
        try {
            return UUID.fromString(profileFileId);
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Invalid ProfileFileId format ID: {}; ERROR: {}", profileFileId, ex.getMessage());
            throw new CustomException("Invalid Profile File ID format", HttpStatus.BAD_REQUEST, ex);
        }
    }

    private void validateUserIdWithContext(UUID userId) {
        if (!contextService.isThisUserId(userId) && !contextService.isAdmin()) {
            LOGGER.error("Unauthorized access attempt by user ID: {}", contextService.getUserId());
            throw new CustomException("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
    }

    private UUID getUserIdFromContext() {
        UUID id = contextService.getUserId();
        if (id == null) {
            LOGGER.error("Request context or user ID is null");
            throw new CustomException("Invalid request context", HttpStatus.UNAUTHORIZED);
        }
        return id;
    }
}
