package com.platform.user_service.services.Ngo;

import com.platform.user_service.dtos.request.NgoCreateRequestDto;
import org.springframework.stereotype.Service;

/**
 * Service interface for NGO registration operations.
 */
@Service
public interface INgoRegisterService {
    /** Method to register a new NGO.
     *
     * @param ngoCreateRequestDto The DTO containing NGO creation details.
     */
    void RegisterNgo(NgoCreateRequestDto ngoCreateRequestDto);
}
