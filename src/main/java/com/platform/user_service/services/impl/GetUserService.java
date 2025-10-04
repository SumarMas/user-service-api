package com.platform.user_service.services.impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.response.UserLoginResponseDto;
import com.platform.user_service.entities.UserEntity;
import com.platform.user_service.repositories.UserRepository;
import com.platform.user_service.services.IGetUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the IGetUserService interface
 * for retrieving user information.
 */
@Service
@AllArgsConstructor
public class GetUserService implements IGetUserService {
    /** Logger for logging information and errors. */
    private static final Logger LOG = LoggerFactory.getLogger(GetUserService.class);
    /** Repository for accessing user data. */
    private final UserRepository userRepository;

    /**
     * Retrieves user login data based on the provided user ID.
     *
     * @param userId the unique identifier of the user
     * @return a UserLoginResponseDto containing user id and roles
     */
    @Override
    public UserLoginResponseDto getDataLoginUser(String userId) {
        LOG.trace("getDataLoginUser()");
        LOG.info("Get info login to userId: {}", userId);
        UUID uuid = validateUserId(userId);
        UserEntity userEntity = getUserEntityById(uuid);
        return buildUserLoginResponseDto(userEntity);
    }

    private UUID validateUserId(String userId) {
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException ex) {
            LOG.error("Invalid UserId format ID: {}; ERROR: {}", userId, ex.getMessage());
            throw new CustomException("Invalid User ID format", HttpStatus.BAD_REQUEST, ex);
        }
    }

    private UserEntity getUserEntityById(UUID userUUID) {
        LOG.trace("Retrieving user entity by ID: {}", userUUID);
        try {
            Optional<UserEntity> optionalUserEntity = userRepository.
                    findByIdAndEnabledIsTrueWithRoles(userUUID);
            if (optionalUserEntity.isEmpty()) {
                LOG.info("User with ID '{}' not found or is disabled.", userUUID.toString());
                throw new CustomException("User not found or is disabled", HttpStatus.NOT_FOUND);
            }
            LOG.trace("User with ID '{}' found.", userUUID.toString());
            return optionalUserEntity.get();
        } catch (DataAccessException ex) {
            LOG.error("Database access error while retrieving user by ID: {}, ERROR: {}",
                    userUUID.toString(), ex.getMessage());
            throw new CustomException("Error retrieving user data", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    private UserLoginResponseDto buildUserLoginResponseDto(UserEntity userEntity) {
        LOG.trace("Building UserLoginResponseDto");
        return UserLoginResponseDto.builder()
                .userId(userEntity.getId())
                .roles(userEntity.getUserRoles().stream()
                        .map(role -> role.getRol().name())
                        .toList())
                .build();
    }
}
