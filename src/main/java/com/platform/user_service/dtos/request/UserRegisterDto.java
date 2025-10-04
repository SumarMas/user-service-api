package com.platform.user_service.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user registration request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {
    /**
     * The first name of the user.
     */
    @NotBlank(message = "firstName cannot be blank")
    @Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
    @JsonProperty("firstName")
    private String firstName;
    /**
     * The last name of the user.
     */
    @NotBlank(message = "lastName cannot be blank")
    @Size(min = 2, max = 50, message = "lastName must be between 2 and 50 characters")
    @JsonProperty("lastName")
    private String lastName;
    /**
     * The email of the user.
     */
    @NotBlank(message = "email cannot be blank")
    @Email(message = "email must be a valid email address")
    @Size(min = 5, max = 255, message = "email must be between 5 and 255 characters")
    @JsonProperty("email")
    private String email;
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
     * The profile file ID of the user.
     */
    @JsonProperty("profileFileId")
    private String profileFileId;
}
