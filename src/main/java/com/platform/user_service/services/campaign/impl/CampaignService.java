package com.platform.user_service.services.campaign.impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.campaign.CampaignDto;
import com.platform.user_service.enums.CampaignState;
import com.platform.user_service.restClients.campaign.ICampaignRestClient;
import com.platform.user_service.services.campaign.ICampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service implementation for managing campaigns.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CampaignService implements ICampaignService {
    /** REST client for interacting with the campaign service. */
    private final ICampaignRestClient campaignRestClient;
    /**
     * Retrieves a list of campaigns based on their state and associated NGO ID.
     *
     * @param state the state of the campaigns to retrieve
     * @param ngoId the unique identifier of the NGO
     * @return a list of CampaignDto objects matching the criteria
     */
    @Override
    public List<CampaignDto> getCampaignsByStateAndNgoId(CampaignState state, UUID ngoId) {
        CampaignDto[] campaignDtos = campaignRestClient.getCampaignsByStateAndNgoId(state, ngoId).getBody();
        if (campaignDtos != null) {
            return List.of(campaignDtos);
        }
       log.error("campaignDtos is null");
        throw new CustomException("Error retrieve campaigns", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
