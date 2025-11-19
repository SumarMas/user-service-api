package com.platform.user_service.services.payout;

import com.platform.user_service.dtos.payout.PayoutRequestDto;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for retrieving payout requests based on NGO ID.
 */
public interface IPayoutGetService {
    /**
     * Retrieves a list of PayoutRequestDto by NGO ID.
     *
     * @param ngoId The unique identifier of the NGO.
     * @return A list of PayoutRequestDto corresponding to the provided NGO ID.
     */
    List<PayoutRequestDto> getPayoutRequestsByNgoId(UUID ngoId);
}
