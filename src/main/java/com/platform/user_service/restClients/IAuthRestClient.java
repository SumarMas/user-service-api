package com.platform.user_service.restClients;

import com.platform.user_service.dtos.common.RegisterAuthDto;
import com.platform.user_service.dtos.response.TokenResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface IAuthRestClient {
    ResponseEntity<TokenResponseDto> registerAuthEntity(RegisterAuthDto registerAuthDto);
}
