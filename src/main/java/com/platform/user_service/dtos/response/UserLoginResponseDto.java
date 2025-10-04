package com.platform.user_service.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object representing a user with their roles.
 */
@Data
@Builder
public class UserLoginResponseDto {
    /**
     * Unique identifier of the user.
     */
    @JsonProperty("userId")
    private UUID userId;
    /**
     * List of roles assigned to the user.
     */
    @JsonProperty("roles")
    private List<String> roles;
}
