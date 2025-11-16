package com.platform.user_service.services.user;

import com.platform.user_service.dtos.common.UserDto;
import com.platform.user_service.dtos.request.UserRegisterDto;
import com.platform.user_service.dtos.request.UserUpdateDto;
import com.platform.user_service.dtos.response.TokenResponseDto;
import com.platform.user_service.dtos.response.UserLoginResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for user-related operations.
 */
@Service
public interface IUserService {
    /**
     * Creates a new user based on the provided registration details.
     *
     * @param user the user registration details
     * @return a TokenResponseDto containing the generated JWT token
     */
    TokenResponseDto crateUser(UserRegisterDto user);

    /**
     * Retrieves user login data based on the provided user ID.
     *
     * @param userId the unique identifier of the user
     * @return a UserLoginResponseDto containing user id and roles
     */
    UserLoginResponseDto getDataLogin(String userId);

    /**
     * Updates the user information based on the provided user ID and update data.
     *
     * @param userId        The UUID of the user to be updated.
     * @param userUpdateDto The DTO containing the updated user information.
     */
    void updateUser(UUID userId, UserUpdateDto userUpdateDto);

    /**
     * Retrieves the profile information of the currently authenticated user.
     *
     * @return a UserDto containing the user's profile details
     */
    UserDto getMyProfile();
    /**
     * Retrieves user information by user ID.
     *
     * @param userId the unique identifier of the user
     * @return a UserDto containing the user's details
     */
    UserDto getUserById(UUID userId);

    /**
     * Retrieves a list of users with admin roles.
     *
     * @return a list of UserDto containing admin users' details
     */
    List<UserDto> getAdminsUsers();
}
