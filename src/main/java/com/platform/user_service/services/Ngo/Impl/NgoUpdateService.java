package com.platform.user_service.services.Ngo.Impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.common.NgoImageDto;
import com.platform.user_service.dtos.request.NgoUpdateRequestDto;
import com.platform.user_service.entities.NgoDocumentEntity;
import com.platform.user_service.entities.NgoEntity;
import com.platform.user_service.entities.NgoImageEntity;
import com.platform.user_service.enums.NgoDocumentStatus;
import com.platform.user_service.enums.NgoStatus;
import com.platform.user_service.repositories.NgoRepository;
import com.platform.user_service.services.IContextService;
import com.platform.user_service.services.Ngo.INgoUpdateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
/**
 * Implementation of the INgoUpdateService interface for updating NGO information.
 */
@Service
@RequiredArgsConstructor
public class NgoUpdateService implements INgoUpdateService {
    /** Logger for logging information and errors. */
    private static final Logger LOG = LoggerFactory.getLogger(NgoUpdateService.class);
    /** Repository for accessing NGO data. */
    private final NgoRepository ngoRepository;
    /** Service for managing context-related operations. */
    private final IContextService contextService;

    /**
     * Updates the information of an existing NGO.
     *
     * @param ngoId   The unique identifier of the NGO to be updated.
     * @param request The DTO containing the updated NGO information.
     */
    @Override
    public void updateNgo(UUID ngoId, NgoUpdateRequestDto request) {
        LOG.trace("In updateNgo");
        UUID userId = getCurrentUserId();
        NgoEntity ngo = getNgoEntity(ngoId);
        validateUserAuthorization(ngo.getUserIdCreator().getId());
        boolean hasChanges = false;
        if (request.getName() != null && !request.getName().equals(ngo.getName())) {
            ngo.setName(request.getName());
            hasChanges = true;
        }
        if (request.getDescription() != null && !request.getDescription().equals(ngo.getDescription())) {
            ngo.setDescription(request.getDescription());
            hasChanges = true;
        }
        if (request.getProfileFileId() != null && !request.getProfileFileId().equals(ngo.getProfileFileId().toString())) {
            ngo.setProfileFileId(uuidConversion(request.getProfileFileId(), "profileFileId"));
            hasChanges = true;
        }
        if (request.getBannerFileId() != null && !request.getBannerFileId().equals(ngo.getBannerFileId().toString())) {
            ngo.setBannerFileId(uuidConversion(request.getBannerFileId(), "bannerFileId"));
            hasChanges = true;
        }
        if (updateDocuments(ngo, request.getDocumentsId(), userId)) {
            ngo.setVerificationStatus(NgoStatus.PENDING);
            hasChanges = true;
        }
        updateImages(ngo, request.getImages(), userId);
        if (hasChanges) {
            ngo.setLastUpdatedUser(userId);
        }
        try {
            ngoRepository.save(ngo);
            LOG.info("NGO with ID {} successfully updated by user {}", ngoId, userId);
        } catch (DataAccessException ex) {
            LOG.error("Database error while updating NGO with ID {}: {}", ngoId, ex.getMessage());
            throw new CustomException("An error occurred while updating NGO.", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    private void validateUserAuthorization(UUID userId) {
        if (!contextService.isThisUserId(userId)) {
            LOG.error("User with ID: {} is not authorized to update NGO", userId);
            throw new CustomException("User is not authorized to update this NGO.", HttpStatus.UNAUTHORIZED);
        }
    }
    private UUID getCurrentUserId() {
        UUID userId = contextService.getUserId();
        if (userId == null) {
            LOG.error("Current user ID not found in context");
            throw  new CustomException("Current user ID not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  userId;
    }

    private NgoEntity getNgoEntity(UUID id) {
        try {
            Optional<NgoEntity> optionalNgoEntity = ngoRepository.findByIdFull(id);
            if (optionalNgoEntity.isEmpty()) {
                LOG.error("NGO with ID: {} not found", id);
                throw new CustomException("NGO not found.", HttpStatus.NOT_FOUND);
            }
            return optionalNgoEntity.get();
        } catch (DataAccessException ex) {
            LOG.error("Database error while retrieving NGO with ID: {}", id, ex);
            throw new CustomException("An error occurred while retrieving NGO.", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    private boolean updateDocuments(NgoEntity ngo, List<String> newDocumentsIds, final UUID userUpdated) {
        boolean hasChanges = false;
        List<NgoDocumentEntity> documents = ngo.getNgoDocuments();
        for (NgoDocumentEntity document : documents) {
            boolean enabled = document.getEnabled();
            boolean existInNew = newDocumentsIds.contains(document.getFileId().toString());
            document.setEnabled(existInNew);
            if (enabled && !existInNew) {
                hasChanges = true;
                document.setLastUpdatedUser(userUpdated);
            }
        }
        for (String newDocumentId : newDocumentsIds) {
            UUID documentUuid = uuidConversion(newDocumentId, "documentId");
            boolean exists = documents.stream().anyMatch(doc -> doc.getId().equals(documentUuid));
            if (!exists) {
                hasChanges = true;
                NgoDocumentEntity newDocument = NgoDocumentEntity.builder()
                        .id(UUID.randomUUID())
                        .ngo(ngo)
                        .fileId(documentUuid)
                        .status(NgoDocumentStatus.RECEIVED)
                        .createdUser(userUpdated)
                        .lastUpdatedUser(userUpdated)
                        .build();
                documents.add(newDocument);
            }
        }
        return hasChanges;
    }

    private void updateImages(NgoEntity ngo, List<NgoImageDto> newImagesIds, final UUID userUpdated) {
        List<NgoImageEntity> images = ngo.getNgoImages();
        for (NgoImageEntity image : images) {
            boolean enabled = image.getEnabled();
            Optional<NgoImageDto> optionalNgoImageDto = newImagesIds.stream()
                    .filter(i -> i.getImageId().equals(image.getFileId().toString())).findFirst();
            boolean existInNew = optionalNgoImageDto.isPresent();
            image.setEnabled(existInNew);
            if (enabled && !existInNew) {
                image.setLastUpdatedUser(userUpdated);
            }
            if (existInNew && !Objects.equals(optionalNgoImageDto.get().getOrderIndex(), image.getOrderIndex())) {
                image.setOrderIndex(optionalNgoImageDto.get().getOrderIndex());
                image.setLastUpdatedUser(userUpdated);
            }
        }
        for (NgoImageDto newImageDto : newImagesIds) {
            UUID imageUuid = uuidConversion(newImageDto.getImageId(), "imageId");
            boolean exists = images.stream().anyMatch(img -> img.getFileId().equals(imageUuid));
            if (!exists) {
                NgoImageEntity newImage = NgoImageEntity.builder()
                        .id(UUID.randomUUID())
                        .ngo(ngo)
                        .fileId(imageUuid)
                        .orderIndex(newImageDto.getOrderIndex())
                        .createdUser(userUpdated)
                        .lastUpdatedUser(userUpdated)
                        .build();
                images.add(newImage);
            }
        }
    }

    private UUID uuidConversion(String id, String fieldName) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            LOG.warn("Invalid UUID format for {}: {}", fieldName, id);
            throw new CustomException("Invalid UUID format for " + fieldName, HttpStatus.BAD_REQUEST, e);
        }
    }
}
