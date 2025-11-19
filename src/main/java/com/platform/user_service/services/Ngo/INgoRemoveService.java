package com.platform.user_service.services.Ngo;

import java.util.UUID;
/**
 * Service interface for removing NGOs.
 */
public interface INgoRemoveService {
    /**
     * Removes an NGO by its unique identifier.
     *
     * @param ngoId the unique identifier of the NGO to be removed
     */
    void removeNgo(UUID ngoId);
}
