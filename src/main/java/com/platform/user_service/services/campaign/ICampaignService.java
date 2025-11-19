package com.platform.user_service.services.campaign;

import com.platform.user_service.dtos.campaign.CampaignDto;
import com.platform.user_service.enums.CampaignState;

import java.util.List;
import java.util.UUID;
/**
 * Service interface for managing campaigns.
 */
public interface ICampaignService {
    /**
     * Retrieves a list of campaigns based on their state and associated NGO ID.
     *
     * @param state the state of the campaigns to retrieve
     * @param ngoId the unique identifier of the NGO
     * @return a list of CampaignDto objects matching the criteria
     */
    List<CampaignDto> getCampaignsByStateAndNgoId(CampaignState state, UUID ngoId);
}
