package com.platform.user_service.services.impl;

import com.platform.user_service.dtos.request.UserRegisterDto;
import com.platform.user_service.dtos.response.TokenResponseDto;
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
    private final IRegisterUserService registerUserService;
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
}
