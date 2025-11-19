package com.platform.user_service.restClients.donation;

import com.platform.user_service.dtos.donation.DonationsDto;
import com.platform.user_service.enums.DonationStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;
import java.util.UUID;
/**
 * REST client interface for interacting with the Donation service.
 */
public interface IDonationRestClient {
    /**
     * Retrieves donations by campaign IDs and status.
     *
     * @param status      the set of donation statuses to filter by
     * @param campaignIds the set of campaign IDs to retrieve donations for
     * @return a ResponseEntity containing a DonationsDto with the filtered donations
     */
    ResponseEntity<DonationsDto> getDonantionsByCampaignsIdAndStatus(Set<DonationStatus> status, Set<UUID> campaignIds);
}
