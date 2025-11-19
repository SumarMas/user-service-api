package com.platform.user_service.restClients.payout;

import com.platform.user_service.dtos.payout.PayoutRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;
/**
 * REST client interface for interacting with the Payout service.
 */
public interface IPayoutRestClient {
    /**
     * Retrieves PayoutRequestDto array by NGO ID.
     *
     * @param ngoId The unique identifier of the NGO.
     * @return A ResponseEntity containing the PayoutRequestDtos
     * corresponding to the provided NGO ID.
     */
    ResponseEntity<PayoutRequestDto[]> getPayoutRequestsByNgoId(UUID ngoId);
}
