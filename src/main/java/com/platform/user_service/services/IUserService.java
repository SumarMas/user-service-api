package com.platform.user_service.services;

import com.platform.user_service.dtos.request.UserRegisterDto;
import com.platform.user_service.dtos.response.TokenResponseDto;
import org.springframework.stereotype.Service;

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
}
