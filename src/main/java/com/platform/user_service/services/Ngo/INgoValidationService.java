package com.platform.user_service.services.Ngo;

import com.platform.user_service.dtos.request.NgoValidationRequestDto;
import org.springframework.stereotype.Service;

import java.util.UUID;
/**
 * Service interface for NGO validation operations.
 */
@Service
public interface INgoValidationService {
    /**
     * Validates an NGO based on the provided request data.
     *
     * @param ngoId      the ID of the NGO to be validated
     * @param requestDto the validation request data
     */
    void validateNgo(UUID ngoId, NgoValidationRequestDto requestDto);
}
