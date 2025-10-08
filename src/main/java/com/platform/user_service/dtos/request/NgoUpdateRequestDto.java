package com.platform.user_service.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.user_service.dtos.common.NgoImageDto;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * Data Transfer Object for updating NGO information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NgoUpdateRequestDto {
    /**
     * Name of the NGO.
     */
    @Size(min = 2, max = 255, message = "name must be between 2 and 255 characters")
    @JsonProperty("name")
    private String name;
    /**
     * Description of the NGO.
     */
    @Size(min = 5, max = 500, message = "description must be between 5 and 500 characters")
    @JsonProperty("description")
    private String description;
    /**
     * The profile file ID of the NGO.
     */
    @JsonProperty("profileFileId")
    private String profileFileId;

    /**
     * The banner file ID of the NGO.
     */
    @JsonProperty("bannerFileId")
    private String bannerFileId;
    /**
     * List of document IDs associated with the NGO.
     */
    @JsonProperty("documentsId")
    private List<String> documentsId;
    /**
     * List of image IDs associated with the NGO.
     */
    @JsonProperty("images")
    private List<NgoImageDto> images;
}
