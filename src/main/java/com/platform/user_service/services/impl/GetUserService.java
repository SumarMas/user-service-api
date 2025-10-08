package com.platform.user_service.services.impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.common.UserDto;
import com.platform.user_service.dtos.response.UserLoginResponseDto;
import com.platform.user_service.entities.UserEntity;
import com.platform.user_service.repositories.UserRepository;
import com.platform.user_service.services.IContextService;
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
    /** Service for context-related operations. */
    private final IContextService contextService;

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

    /**
     * Retrieves the profile information of the currently authenticated user.
     *
     * @return a UserDto containing the user's profile details
     */
    @Override
    public UserDto getMyProfile() {
        LOG.trace("getMyProfile()");
        UUID userID = getCurrentUserId();
        UserEntity userEntity = getUserEntityById(userID);
        LOG.trace("Building UserDto");
        return buildUserDto(userEntity);
    }

    private UUID validateUserId(String userId) {
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException ex) {
            LOG.error("Invalid UserId format ID: {}; ERROR: {}", userId, ex.getMessage());
            throw new CustomException("Invalid User ID format", HttpStatus.BAD_REQUEST, ex);
        }
    }

    private UUID getCurrentUserId() {
        UUID userId =  contextService.getUserId();
        if (userId == null) {
            LOG.warn("User ID not found in context");
            throw new CustomException("User ID not found in context", HttpStatus.UNAUTHORIZED);
        }
        return userId;
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

    private UserDto buildUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId().toString())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .profileFileId(userEntity.getProfileFileId() != null ?
                        userEntity.getProfileFileId().toString() : null)
                .status(userEntity.getStatus().name())
                .roles(userEntity.getUserRoles().stream()
                        .map(role -> role.getRol().name())
                        .toList())
                .build();
    }
}
