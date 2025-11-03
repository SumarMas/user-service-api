package com.platform.user_service.services.Ngo.Impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.request.NgoValidationRequestDto;
import com.platform.user_service.entities.NgoDocumentEntity;
import com.platform.user_service.entities.NgoEntity;
import com.platform.user_service.enums.NgoDocumentStatus;
import com.platform.user_service.enums.NgoStatus;
import com.platform.user_service.repositories.NgoDocumentRepository;
import com.platform.user_service.repositories.NgoRepository;
import com.platform.user_service.services.user.IContextService;
import com.platform.user_service.services.Ngo.INgoValidationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the INgoValidationService
 * interface for NGO validation operations.
 */
@Service
@RequiredArgsConstructor
public class NgoValidationService implements INgoValidationService {
    /** Logger for logging information and errors. */
    private static final Logger LOG = LoggerFactory.getLogger(NgoValidationService.class);
    /** Repository for accessing NGO data. */
    private final NgoRepository ngoRepository;
    /** Repository for accessing NGO document data. */
    private final NgoDocumentRepository ngoDocumentRepository;
    /** Service for managing context-related operations. */
    private final IContextService contextService;
    /**
     * Validates an NGO based on the provided request data.
     *
     * @param ngoId      the ID of the NGO to be validated
     * @param requestDto the validation request data
     */
    @Override
    @Transactional
    public void validateNgo(UUID ngoId, NgoValidationRequestDto requestDto) {
        LOG.trace("Validating NGO");
        checkAdmin();
        UUID adminId = getCurrentUserId();
        NgoEntity ngo = findNgoById(ngoId);
        List<NgoDocumentEntity> documents = findNgoDocumentsByNgoId(ngoId);
        NgoStatus ngoStatus = requestDto.isApproved() ? NgoStatus.VERIFIED : NgoStatus.DENIED;
        NgoDocumentStatus documentStatus = requestDto.isApproved() ? NgoDocumentStatus.ACCEPTED : NgoDocumentStatus.REJECTED;
        documents.forEach(document -> {
                document.setStatus(documentStatus);
                document.setAdminComment(requestDto.getComment());
                document.setLastUpdatedUser(adminId);
        });
        ngo.setVerificationStatus(ngoStatus);
        ngo.setLastUpdatedUser(adminId);
        ngoRepository.save(ngo);
        ngoDocumentRepository.saveAll(documents);
        LOG.info("NGO with id {} validated successfully", ngoId);
    }

    private void checkAdmin() {
        if (!contextService.isAdmin()) {
            LOG.warn("User is not admin");
            throw new CustomException("User is unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    private NgoEntity findNgoById(UUID id) {
        Optional<NgoEntity> optional = ngoRepository.findByIdAndEnabledTrue(id);
        if (optional.isEmpty()) {
            LOG.warn("Ngo with id {} not found", id);
            throw new CustomException("Ngo not found", HttpStatus.NOT_FOUND);
        }
        return optional.get();
    }

    private List<NgoDocumentEntity> findNgoDocumentsByNgoId(UUID ngoId) {
        List<NgoDocumentEntity> documents = ngoDocumentRepository.findAllByNgoIdAndStatusAndEnabled(ngoId, NgoDocumentStatus.RECEIVED);
        if (documents.isEmpty()) {
            LOG.warn("No documents found for NGO with id {}", ngoId);
            throw new CustomException("No documents found for NGO", HttpStatus.NOT_FOUND);
        }
        return documents;
    }

    private UUID getCurrentUserId() {
        UUID userId = contextService.getUserId();
        if (userId == null) {
            LOG.error("Current user ID not found in context");
            throw  new CustomException("Current user ID not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  userId;
    }
}
