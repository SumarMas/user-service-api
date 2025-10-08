package com.platform.user_service.services.Ngo;

import com.platform.user_service.dtos.common.NgoDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for handling NGO retrieval operations.
 */
@Service
public interface INgoGetService {
    /**
     * Retrieves a list of all NGOs approved.
     *
     * @return a list of NgoDto representing all NGOs
     */
    List<NgoDto> getAllApproveNgos();

    /**
     * Retrieves the details of a specific NGO by its ID.
     *
     * @param ngoId the unique identifier of the NGO
     * @return the NgoDto representing the NGO details
     */
    NgoDto getNgoById(UUID ngoId);

    /**
     * Retrieves the NGO associated with the currently authenticated user.
     *
     * @return the NgoDto representing the user's NGO details
     */
    NgoDto getMyNgo();

    /**
     * Retrieves a list of NGOs pending approval.
     *
     * @return a list of NgoDto representing the pending NGOs
     */
    List<NgoDto> getPendingNgos();
}
