package com.platform.user_service.services.Ngo;

import com.platform.user_service.dtos.request.NgoUpdateRequestDto;
import org.springframework.stereotype.Service;

import java.util.UUID;
/**
 * Service interface for updating NGO information.
 */
@Service
public interface INgoUpdateService {
    /**
     * Updates the information of an existing NGO.
     *
     * @param ngoId   The unique identifier of the NGO to be updated.
     * @param request The DTO containing the updated NGO information.
     */
    void updateNgo(UUID ngoId, NgoUpdateRequestDto request);
}
