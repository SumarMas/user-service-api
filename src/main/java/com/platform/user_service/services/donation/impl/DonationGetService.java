package com.platform.user_service.services.donation.impl;

import com.platform.user_service.dtos.donation.DonationsDto;
import com.platform.user_service.enums.DonationStatus;
import com.platform.user_service.restClients.donation.IDonationRestClient;
import com.platform.user_service.services.donation.IDonationGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Service implementation for retrieving donation information.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DonationGetService implements IDonationGetService {
    /** REST client for interacting with the donation service. */
    private final IDonationRestClient donationRestClient;
    /**
     * Retrieves donations filtered by their statuses and associated NGO campaign IDs.
     *
     * @param status      A set of donation statuses to filter by.
     * @param campaignIds A set of NGO campaign IDs to filter donations.
     * @return A DonationsDto matching the specified criteria.
     */
    @Override
    public DonationsDto getDonationsByStatesAndNgosId(Set<DonationStatus> status, Set<UUID> campaignIds) {
        DonationsDto donationsDto = donationRestClient.getDonantionsByCampaignsIdAndStatus(status, campaignIds).getBody();
        if (Objects.isNull(donationsDto)) {
            return DonationsDto.builder()
                    .donations(new ConcurrentHashMap<>())
                    .build();
        }
        return donationsDto;
    }
}
