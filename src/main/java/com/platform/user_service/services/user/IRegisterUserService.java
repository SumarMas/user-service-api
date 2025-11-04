package com.platform.user_service.services.user;

import com.platform.user_service.dtos.request.UserRegisterDto;
import com.platform.user_service.dtos.response.TokenResponseDto;

/**
 * Service interface for user registration operations.
 */
public interface IRegisterUserService {
    /**
     * Registers a new user based on the provided registration details.
     *
     * @param registerUserDto the user registration details
     * @return a TokenResponseDto containing the generated JWT token
     */
    TokenResponseDto registerUser(UserRegisterDto registerUserDto);
}
