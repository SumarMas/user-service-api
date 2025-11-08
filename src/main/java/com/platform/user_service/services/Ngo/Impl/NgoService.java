package com.platform.user_service.services.Ngo.Impl;

import com.platform.user_service.dtos.request.NgoCreateRequestDto;
import com.platform.user_service.dtos.request.NgoUpdateRequestDto;
import com.platform.user_service.dtos.request.NgoValidationRequestDto;
import com.platform.user_service.dtos.common.NgoDto;
import com.platform.user_service.services.Ngo.INgoGetService;
import com.platform.user_service.services.Ngo.INgoRegisterService;
import com.platform.user_service.services.Ngo.INgoService;
import com.platform.user_service.services.Ngo.INgoUpdateService;
import com.platform.user_service.services.Ngo.INgoValidationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service implementation for managing NGOs.
 * Handles operations related to NGO registration and management.
 */
@Service
@RequiredArgsConstructor
public class NgoService implements INgoService {
    /** Logger for logging information and errors. */
    private static final Logger LOG = LoggerFactory.getLogger(NgoService.class);
    /** Service for handling NGO registration operations. */
    private final INgoRegisterService ngoRegisterService;
    /** Service for handling Get NGO operations. */
    private final INgoGetService ngoGetService;
    /** Service for handling NGO validation operations. */
    private final INgoValidationService ngoValidationService;
    /** Service for handling NGO update operations. */
    private final INgoUpdateService ngoUpdateService;

    /**
     * Method to register a new NGO.
     *
     * @param ngoCreateRequestDto The DTO containing NGO creation details.
     */
    @Override
    public void registerNgo(NgoCreateRequestDto ngoCreateRequestDto) {
        LOG.trace("In RegisterNgo");
        ngoRegisterService.registerNgo(ngoCreateRequestDto);
    }

    /**
     * Updates the information of an existing NGO.
     *
     * @param ngoId   The unique identifier of the NGO to be updated.
     * @param request The DTO containing the updated NGO information.
     */
    @Override
    public void updateNgo(UUID ngoId, NgoUpdateRequestDto request) {
        LOG.trace("In UpdateNgo");
        ngoUpdateService.updateNgo(ngoId, request);
    }

    /**
     * Retrieves a list of pending validation NGOs.
     *
     * @return a list of NgoPendingDto representing the pending NGOs
     */
    @Override
    public List<NgoDto> getPending() {
        LOG.trace("In getPending");
        return ngoGetService.getPendingNgos();
    }

    /**
     * Validates an NGO based on the provided request data.
     *
     * @param ngoId      the ID of the NGO to be validated
     * @param requestDto the validation request data
     */
    @Override
    public void validateNgo(UUID ngoId, NgoValidationRequestDto requestDto) {
        LOG.trace("In validateNgo");
        ngoValidationService.validateNgo(ngoId, requestDto);
    }

    /**
     * Retrieves a list of all NGOs approved.
     *
     * @return a list of NgoDto representing all NGOs
     */
    @Override
    public List<NgoDto> getAllApproveNgos() {
        LOG.trace("In getAllApproveNgos");
        return ngoGetService.getAllApproveNgos();
    }

    /**
     * Retrieves the details of a specific NGO by its ID.
     *
     * @param ngoId the unique identifier of the NGO
     * @return the NgoDto representing the NGO details
     */
    @Override
    public NgoDto getNgoById(UUID ngoId) {
        LOG.trace("In getNgoById");
        return ngoGetService.getNgoById(ngoId);
    }

    /**
     * Retrieves the NGO associated with the currently authenticated user.
     *
     * @return the NgoDto representing the user's NGO details
     */
    @Override
    public NgoDto getMyNgo() {
        LOG.trace("In getMyNgo");
        return ngoGetService.getMyNgo();
    }

    /**
     * Retrieves a list of all NGOs.
     *
     * @return a list of NgoDto representing all NGOs
     */
    @Override
    public List<NgoDto> getAllNgos() {
        LOG.trace("In getAllNgos");
        return ngoGetService.getAllNgos();
    }
}
