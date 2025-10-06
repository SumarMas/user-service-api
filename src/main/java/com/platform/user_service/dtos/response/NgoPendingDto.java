package com.platform.user_service.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.user_service.dtos.common.NgoImageDto;
import com.platform.user_service.dtos.common.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NgoPendingDto {
    /**
     * Unique identifier for the NGO.
     */
    @JsonProperty("ngoId")
    private String id;
    /**
     * The user who created the NGO entry.
     */
    @JsonProperty("userCreator")
    private UserDto userCreator;
    /**
     * Name of the NGO.
     */
    @JsonProperty("name")
    private String name;
    /**
     * Description of the NGO.
     */
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

    /**
     * The creation date and time of the NGO entry.
     */
    @JsonProperty("createdDateTime")
    private LocalDateTime createdDateTime;
}
