package com.platform.user_service.services.Ngo.Impl;

import com.platform.user_service.dtos.request.NgoCreateRequestDto;
import com.platform.user_service.services.Ngo.INgoRegisterService;
import com.platform.user_service.services.Ngo.INgoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    /**
     * Method to register a new NGO.
     *
     * @param ngoCreateRequestDto The DTO containing NGO creation details.
     */
    @Override
    public void registerNgo(NgoCreateRequestDto ngoCreateRequestDto) {
        LOG.trace("In RegisterNgo");
        ngoRegisterService.RegisterNgo(ngoCreateRequestDto);
    }
}
