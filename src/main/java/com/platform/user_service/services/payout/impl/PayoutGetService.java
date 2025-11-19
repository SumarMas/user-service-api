package com.platform.user_service.services.payout.impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.payout.PayoutRequestDto;
import com.platform.user_service.restClients.payout.IPayoutRestClient;
import com.platform.user_service.services.payout.IPayoutGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
/**
 * Service implementation for retrieving payout requests.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PayoutGetService implements IPayoutGetService {
    /**
     * REST client for interacting with the payout service.
     */
    private final IPayoutRestClient payoutRestClient;

    /**
     * Retrieves a list of PayoutRequestDto by NGO ID.
     *
     * @param ngoId The unique identifier of the NGO.
     * @return A list of PayoutRequestDto corresponding to the provided NGO ID.
     */
    @Override
    public List<PayoutRequestDto> getPayoutRequestsByNgoId(UUID ngoId) {
        log.trace("getPayoutRequestsByNgoId({})", ngoId);
        PayoutRequestDto[] payoutRequestDtos =
                payoutRestClient.getPayoutRequestsByNgoId(ngoId).getBody();
        if (payoutRequestDtos != null) {
            return List.of(payoutRequestDtos);
        }
        log.warn("payoutRequestDtos is null");
        throw new CustomException("Error retrieve payout requests", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
