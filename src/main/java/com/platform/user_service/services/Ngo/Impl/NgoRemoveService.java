package com.platform.user_service.services.Ngo.Impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.campaign.CampaignDto;
import com.platform.user_service.dtos.donation.DonationsDto;
import com.platform.user_service.dtos.payout.PayoutRequestDto;
import com.platform.user_service.entities.NgoEntity;
import com.platform.user_service.enums.CampaignState;
import com.platform.user_service.enums.DonationStatus;
import com.platform.user_service.enums.PayoutStatus;
import com.platform.user_service.enums.UserRole;
import com.platform.user_service.repositories.NgoRepository;
import com.platform.user_service.services.Ngo.INgoRemoveService;
import com.platform.user_service.services.campaign.ICampaignService;
import com.platform.user_service.services.donation.IDonationGetService;
import com.platform.user_service.services.payout.IPayoutGetService;
import com.platform.user_service.services.user.IContextService;
import com.platform.user_service.services.user.IUpdateUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
/**
 * Service implementation for removing NGOs.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NgoRemoveService implements INgoRemoveService {
    /** Service for managing campaigns. */
    private final ICampaignService campaignService;
    /** Service for retrieving donation information. */
    private final IDonationGetService donationGetService;
    /** Service for retrieving payout information. */
    private final IPayoutGetService payoutGetService;
    /** Service for managing user context. */
    private final IContextService contextService;
    /** Repository for accessing NGO data. */
    private final NgoRepository ngoRepository;
    /** Service for updating user information. */
    private final IUpdateUserService updateUserService;

    /**
     * Removes an NGO by its unique identifier.
     *
     * @param ngoId the unique identifier of the NGO to be removed
     */
    @Override
    @Transactional
    public void removeNgo(UUID ngoId) {
        NgoEntity ngoEntity = getNgoEntityById(ngoId);
        UUID ownerId = ngoEntity.getUserIdCreator().getId();
        hasPermissionToRemoveNgo(ownerId);
        List<CampaignDto> campaigns = getAllCampaignsOfNgo(ngoId);
        validateCampaignsActive(campaigns);
        DonationsDto donationsDto = getAllDonationPending(campaigns);
        validateDonationsPending(donationsDto);
        List<PayoutRequestDto> payoutRequestDtos = getAllPayoutsPending(ngoId);
        validatePayoutsPending(payoutRequestDtos);
        deactivateNgo(ngoEntity);
    }

    private UUID getUserContextId() {
        return contextService.getUserId();
    }

    private void hasPermissionToRemoveNgo(UUID ownerId) {
        if (!contextService.isThisUserId(ownerId) && !contextService.isAdmin()) {
            log.warn("User {} does not have permission to remove NGO owned by {}", getUserContextId(), ownerId);
            throw new CustomException("User does not have permission to remove NGO", HttpStatus.FORBIDDEN);
        }
    }

    private NgoEntity getNgoEntityById(UUID ngoId) {
        Optional<NgoEntity> ngoEntity = ngoRepository.findByIdFull(ngoId);
        if (ngoEntity.isEmpty()) {
            log.warn("No NGO found with ID: {}", ngoId);
            throw new CustomException("NGO not found", HttpStatus.NOT_FOUND);
        }
        return ngoEntity.get();
    }

    private List<CampaignDto> getAllCampaignsOfNgo(UUID ngoId) {
        return campaignService.getCampaignsByStateAndNgoId(null, ngoId);
    }

    private List<PayoutRequestDto> getAllPayoutsPending(UUID ngoId) {
        return payoutGetService.getPayoutRequestsByNgoId(ngoId).stream()
                .filter(p -> p.getStatus().equals(PayoutStatus.PENDING)).toList();
    }

    private DonationsDto getAllDonationPending(List<CampaignDto> campaigns) {
        Set<UUID> campaignIds = campaigns.stream()
                .map(campaignDto -> UUID.fromString(campaignDto.getId()))
                .collect(java.util.stream.Collectors.toSet());
        return donationGetService.getDonationsByStatesAndNgosId(Set.of(DonationStatus.CONFIRMED), campaignIds);
    }

    /**
     * Validates that there are no active campaigns in the provided list.
     *
     * @param campaigns the list of campaigns to validate
     * @throws CustomException with BAD_REQUEST status if there are active campaigns
     */
    private void validateCampaignsActive(List<CampaignDto> campaigns) {
        boolean hasActiveCampaigns = campaigns.stream()
                .anyMatch(campaign -> campaign.getCampaignState().equals(CampaignState.ACTIVE));
        if (hasActiveCampaigns) {
            log.warn("NGO has active campaigns and cannot be removed");
            throw new CustomException("NGO has active campaigns and cannot be removed", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Validates that there are no pending donations in the provided DonationsDto.
     *
     * @param donationsDto the DonationsDto to validate
     * @throws CustomException with BAD_REQUEST status if there are pending donations
     */
    private void validateDonationsPending(DonationsDto donationsDto) {
        boolean hasPendingDonations = !donationsDto.getDonations().isEmpty();
        if (hasPendingDonations) {
            log.warn("NGO has pending donations and cannot be removed");
            throw new CustomException("NGO has pending donations and cannot be removed", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Validates that there are no pending payouts in the provided list.
     *
     * @param payoutRequestDtos the list of payout requests to validate
     * @throws CustomException with BAD_REQUEST status if there are pending payouts
     */
    private void validatePayoutsPending(List<PayoutRequestDto> payoutRequestDtos) {
        boolean hasPendingPayouts = !payoutRequestDtos.isEmpty();
        if (hasPendingPayouts) {
            log.warn("NGO has pending payouts and cannot be removed");
            throw new CustomException("NGO has pending payouts and cannot be removed", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deactivates the specified NGO entity.
     *
     * @param ngoEntity the NGO entity to deactivate
     * @throws CustomException with INTERNAL_SERVER_ERROR status
     * if there is an error during deactivation
     */
    private void deactivateNgo(NgoEntity ngoEntity) {
        ngoEntity.setEnabled(false);
        ngoEntity.setLastUpdatedUser(getUserContextId());
        updateUserService.removeRoleFromUser(ngoEntity.getUserIdCreator().getId(), UserRole.ORGANIZATION);
        try {
            ngoRepository.save(ngoEntity);
        } catch (DataAccessException ex) {
            log.error("Error deactivating NGO with ID: {}", ngoEntity.getId(), ex);
            throw new CustomException("Error deactivating NGO", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }
}
