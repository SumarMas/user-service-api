package com.platform.user_service.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto {
    /**
     * The first name of the user.
     */
    @Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
    @JsonProperty("firstName")
    private String firstName;
    /**
     * The last name of the user.
     */
    @Size(min = 2, max = 50, message = "lastName must be between 2 and 50 characters")
    @JsonProperty("lastName")
    private String lastName;

    /**
     * The profile file ID of the user.
     */
    @JsonProperty("profileFileId")
    private String profileFileId;
}
