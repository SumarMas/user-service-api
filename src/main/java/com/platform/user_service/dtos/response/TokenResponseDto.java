package com.platform.user_service.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for token response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDto {
    /**
     * Token string.
     */
    private String token;

    /**
     * Expiration time in seconds.
     */
    @JsonProperty("expiresIn")
    private long expiresIn;
}