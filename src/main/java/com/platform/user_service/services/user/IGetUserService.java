package com.platform.user_service.services.user;

import com.platform.user_service.dtos.common.UserDto;
import com.platform.user_service.dtos.response.UserLoginResponseDto;
import org.springframework.stereotype.Service;

/**
 * Service interface for retrieving user information.
 */
@Service
public interface IGetUserService {
    /**
     * Retrieves user login data based on the provided user ID.
     *
     * @param userId the unique identifier of the user
     * @return a UserLoginResponseDto containing user id and roles
     */
    UserLoginResponseDto getDataLoginUser(String userId);
    /**
     * Retrieves the profile information of the currently authenticated user.
     *
     * @return a UserDto containing the user's profile details
     */
    UserDto getMyProfile();
}
