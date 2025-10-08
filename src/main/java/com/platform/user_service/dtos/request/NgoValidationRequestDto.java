package com.platform.user_service.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for NGO validation request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NgoValidationRequestDto {
    /** Indicates whether the NGO is approved or not. */
    @JsonProperty("approved")
    private boolean approved;
    /** Additional comments regarding the validation. */
    @JsonProperty("comment")
    private String comment;
}
