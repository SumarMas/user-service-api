package com.platform.user_service.services.Ngo;

import com.platform.user_service.dtos.request.NgoCreateRequestDto;
import com.platform.user_service.dtos.request.NgoUpdateRequestDto;
import com.platform.user_service.dtos.request.NgoValidationRequestDto;
import com.platform.user_service.dtos.response.NgoPendingDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for handle NGO operations.
 */
@Service
public interface INgoService {
    /** Method to register a new NGO.
     *
     * @param ngoCreateRequestDto The DTO containing NGO creation details.
     */
    void registerNgo(NgoCreateRequestDto ngoCreateRequestDto);

    /**
     * Updates the information of an existing NGO.
     *
     * @param ngoId   The unique identifier of the NGO to be updated.
     * @param request The DTO containing the updated NGO information.
     */
    void updateNgo(UUID ngoId, NgoUpdateRequestDto request);

    /**
     * Retrieves a list of pending validation NGOs.
     *
     * @return a list of NgoPendingDto representing the pending NGOs
     */
    List<NgoPendingDto> getPending();

    /**
     * Validates an NGO based on the provided request data.
     *
     * @param ngoId      the ID of the NGO to be validated
     * @param requestDto the validation request data
     */
    void validateNgo(UUID ngoId, NgoValidationRequestDto requestDto);
}
