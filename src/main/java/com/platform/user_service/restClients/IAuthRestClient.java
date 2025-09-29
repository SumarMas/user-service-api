package com.platform.user_service.restClients;

import com.platform.user_service.dtos.common.RegisterAuthDto;
import com.platform.user_service.dtos.response.TokenResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service interface for authentication REST client.
 */
@Service
public interface IAuthRestClient {
    /**
     * Registers a new user by sending a POST request to the auth service.
     *
     * @param registerAuthDto the registration details
     * @return a ResponseEntity containing
     * the TokenResponseDto with the generated JWT token
     */
    ResponseEntity<TokenResponseDto> registerAuthEntity(RegisterAuthDto registerAuthDto);
}
