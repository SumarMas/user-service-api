package com.platform.user_service.services.user.impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.common.RegisterAuthDto;
import com.platform.user_service.dtos.common.UserDto;
import com.platform.user_service.dtos.request.UserRegisterDto;
import com.platform.user_service.dtos.response.TokenResponseDto;
import com.platform.user_service.entities.UserEntity;
import com.platform.user_service.entities.UserRoleEntity;
import com.platform.user_service.entities.embeddable.UserRolId;
import com.platform.user_service.enums.UserRole;
import com.platform.user_service.enums.UserStatus;
import com.platform.user_service.repositories.UserRepository;
import com.platform.user_service.restClients.IAuthRestClient;
import com.platform.user_service.services.user.INewUserPublishEventService;
import com.platform.user_service.services.user.IRegisterUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Implementation of the IRegisterUserService
 * interface for user registration operations.
 */
@Service
@AllArgsConstructor
public class RegisterUserService implements IRegisterUserService {

    /** Logger for logging information and errors. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterUserService.class);
    /** REST client for interacting with the authentication service. */
    private final IAuthRestClient authRestClient;
    /** Repository for accessing user data. */
    private final UserRepository userRepository;
    /** The default role assigned to newly registered users. */
    private static final  UserRole DEFAULT_ROLE = UserRole.DONOR;
    /** Service for publishing events when a new user is created. */
    private final INewUserPublishEventService newUserPublishEventService;

    /**
     * Registers a new user based on the provided registration details.
     *
     * @param registerUserDto the user registration details
     * @return a TokenResponseDto containing the generated JWT token
     */
    @Override
    @Transactional
    public TokenResponseDto registerUser(UserRegisterDto registerUserDto) {

        LOGGER.info("In registerUser method");

        validateEmail(registerUserDto.getEmail());
        LOGGER.trace("Email validated: {}", registerUserDto.getEmail());
        UserEntity userEntity = buildUserEntity(registerUserDto);
        LOGGER.trace("User entity built: {}", userEntity);
        UserRoleEntity userRoleEntity = buildUserRoleEntity(userEntity);
        userEntity.getUserRoles().add(userRoleEntity);
        LOGGER.trace("User role entity built: {}", userRoleEntity);
        userRepository.save(userEntity);
        userRepository.flush();
        //Todo: Remove userEntity flush and implement a more robust
        // rollback mechanism if auth service registration fails
        LOGGER.trace("User entity saved: {}", userEntity);
        RegisterAuthDto registerAuthDto = buildRegisterAuthDto(registerUserDto, userEntity.getId());
        TokenResponseDto token = registerInAuthService(registerAuthDto);
        if (token == null) {
            LOGGER.error("In registerUser method, token is null; rolling back user creation");
            throw new CustomException("Error registering user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        sendEventUserCreated(userEntity);
        return token;
    }

    private void validateEmail(String email) {
        LOGGER.trace("In validateEmail method");
        if (userRepository.existsByEmail(email)) {
            LOGGER.warn("Email is already in use : {}", email);
            throw new CustomException("Email is already in use", HttpStatus.CONFLICT);
        }
    }

    private UserEntity buildUserEntity(UserRegisterDto registerUserDto) {
        LOGGER.trace("In buildUserEntity method");
        UUID userId = UUID.randomUUID();
        return UserEntity.builder()
                .id(userId)
                .firstName(registerUserDto.getFirstName())
                .lastName(registerUserDto.getLastName())
                .email(registerUserDto.getEmail())
                .status(UserStatus.ACTIVE)
                .profileFileId(parseProfileFileId(registerUserDto.getProfileFileId()))
                .userRoles(new ArrayList<>())
                .createdUser(userId)
                .lastUpdatedUser(userId)
                .enabled(true)
                .build();
    }

    private UserRoleEntity buildUserRoleEntity(UserEntity userEntity) {
        LOGGER.trace("In buildUserRoleEntity method");
        final UUID userId = userEntity.getId();
        return UserRoleEntity.builder()
                .id(new UserRolId(userId, DEFAULT_ROLE))
                .createdUser(userId)
                .lastUpdatedUser(userId)
                .user(userEntity)
                .build();
    }

    private RegisterAuthDto buildRegisterAuthDto(UserRegisterDto registerUserDto, UUID userId) {
        LOGGER.trace("In buildRegisterAuthDto method");
        return RegisterAuthDto.builder()
                .userId(userId)
                .username(registerUserDto.getUsername())
                .password(registerUserDto.getPassword())
                .defaultRole(DEFAULT_ROLE.toString())
                .build();
    }

    private TokenResponseDto registerInAuthService(RegisterAuthDto registerAuthDto) {
        LOGGER.trace("In registerInAuthService method");
        ResponseEntity<TokenResponseDto> response = authRestClient.registerAuthEntity(registerAuthDto);
        LOGGER.trace("Response from auth service: {}", response);
        return response.getBody();
    }

    private UUID parseProfileFileId(String profileFileId) {
        try {
            if (profileFileId == null || profileFileId.isBlank()) {
                return null;
            }
            return UUID.fromString(profileFileId);
        } catch (IllegalArgumentException ex) {
            LOGGER.warn("Invalid profileFileId format: {} | Error: {}", profileFileId, ex.getMessage());
            throw new CustomException("Invalid profileFileId", HttpStatus.BAD_REQUEST, ex);
        }
    }

    private void sendEventUserCreated(UserEntity userEntity) {
        LOGGER.trace("In sendEventUserCreated method");
        UserDto userDto = UserDto.builder()
                .id(userEntity.getId().toString())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .build();
        newUserPublishEventService.publishNewUserEvent(userDto);
    }
}
