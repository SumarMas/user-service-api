package com.platform.user_service.dtos.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for user registration request.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterAuthDto {
    /**
     * The username of the user attempting to log in.
     */
    @NotBlank(message = "username cannot be blank")
    @Size(min = 4, max = 100, message = "username must be between 4 and 100 characters")
    @JsonProperty("userName")
    private String username;

    /**
     * The password of the user attempting to log in.
     */
    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, max = 32, message = "password must be between 6 and 32 characters")
    @JsonProperty("password")
    private String password;

    /**
     * The unique identifier of the user vinculated.
     */
    @NotNull(message = "user_id is required")
    @JsonProperty("userId")
    private UUID userId;

    /**
     * The default role assigned to the user upon registration.
     */
    @NotNull(message = "default_role is required")
    @JsonProperty("defaultRole")
    private String defaultRole;
}
