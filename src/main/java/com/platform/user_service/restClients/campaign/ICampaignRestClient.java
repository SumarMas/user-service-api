package com.platform.user_service.restClients.campaign;

import com.platform.user_service.dtos.campaign.CampaignDto;
import com.platform.user_service.enums.CampaignState;
import org.springframework.http.ResponseEntity;

import java.util.UUID;
/**
 * REST client interface for interacting with the Campaign service.
 */
public interface ICampaignRestClient {

    /**
     * Retrieves a CampaignDto by its state and associated NGO ID.
     *
     * @param state The state of the campaign.
     * @param ngoId The unique identifier of the associated NGO.
     * @return A ResponseEntity containing the CampaignDtos
     * corresponding to the provided state and NGO ID.
     */
    ResponseEntity<CampaignDto[]> getCampaignsByStateAndNgoId(CampaignState state, UUID ngoId);
}
