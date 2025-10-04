package com.platform.user_service.services.impl;

import com.platform.user_service.dtos.request.UserRegisterDto;
import com.platform.user_service.dtos.response.TokenResponseDto;
import com.platform.user_service.dtos.response.UserLoginResponseDto;
import com.platform.user_service.services.IGetUserService;
import com.platform.user_service.services.IRegisterUserService;
import com.platform.user_service.services.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the IUserService interface for user-related operations.
 */
@Service
@AllArgsConstructor
public class UserService implements IUserService {
    /** Service for handling user registration operations. */
    private final IRegisterUserService registerUserService;
    /** Service for handling get user information operations.*/
    private final IGetUserService getUserService;

    /**
     * Creates a new user based on the provided registration details.
     *
     * @param user the user registration details
     * @return a TokenResponseDto containing the generated JWT token
     */
    @Override
    public TokenResponseDto crateUser(UserRegisterDto user) {
        return registerUserService.registerUser(user);
    }

    /**
     * Retrieves user login data based on the provided user ID.
     *
     * @param userId the unique identifier of the user
     * @return a UserLoginResponseDto containing user id and roles
     */
    @Override
    public UserLoginResponseDto getDataLogin(String userId) {
        return getUserService.getDataLoginUser(userId);
    }
}
