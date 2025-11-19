package com.platform.user_service.services.donation;

import com.platform.user_service.dtos.donation.DonationsDto;
import com.platform.user_service.enums.DonationStatus;

import java.util.Set;
import java.util.UUID;
/**
 * Service interface for managing donation-related operations.
 */
public interface IDonationGetService {
    /**
     * Retrieves donations filtered by their statuses and associated NGO campaign IDs.
     *
     * @param status      A set of donation statuses to filter by.
     * @param campaignIds A set of NGO campaign IDs to filter donations.
     * @return A list of DonationsDto matching the specified criteria.
     */
    DonationsDto getDonationsByStatesAndNgosId(Set<DonationStatus> status, Set<UUID> campaignIds);
}
