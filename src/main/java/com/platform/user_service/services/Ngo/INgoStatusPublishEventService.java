package com.platform.user_service.services.Ngo;

import com.platform.user_service.enums.NgoStatus;

import java.util.UUID;
/**
 * Service interface for publishing NGO status events.
 */
public interface INgoStatusPublishEventService {
    /** Publishes an NGO status event.
     *
     * @param userId            The UUID of the user associated with the NGO.
     * @param ngoName          The name of the NGO.
     * @param ngoStatus        The status of the NGO.
     * @param ngoStatusMessage Additional message related to the NGO status.
     */
    void publishNgoStatusEvent(UUID userId, String ngoName, NgoStatus ngoStatus, String ngoStatusMessage);
}
