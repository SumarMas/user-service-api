package com.platform.user_service.services.Ngo.Impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.common.NgoImageDto;
import com.platform.user_service.dtos.request.NgoCreateRequestDto;
import com.platform.user_service.entities.NgoDocumentEntity;
import com.platform.user_service.entities.NgoEntity;
import com.platform.user_service.entities.NgoImageEntity;
import com.platform.user_service.entities.UserEntity;
import com.platform.user_service.enums.NgoDocumentStatus;
import com.platform.user_service.enums.NgoStatus;
import com.platform.user_service.enums.UserRole;
import com.platform.user_service.repositories.NgoRepository;
import com.platform.user_service.services.IContextService;
import com.platform.user_service.services.IUpdateUserService;
import com.platform.user_service.services.Ngo.INgoRegisterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the NGO registration service.
 */
@Service
@RequiredArgsConstructor
public class NgoRegisterService implements INgoRegisterService {
    /** Logger instance for logging purposes. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NgoRegisterService.class);
    /** Repository for NGO entities. */
    private final NgoRepository ngoRepository;
    /** Service for updating user information. */
    private final IUpdateUserService updateUserService;
    /** Service for context-related operations. */
    private final IContextService contextService;
    /**
     * Method to register a new NGO.
     *
     * @param ngoCreateRequestDto The DTO containing NGO creation details.
     */
    @Override
    @Transactional
    public void registerNgo(NgoCreateRequestDto ngoCreateRequestDto) {
        try {
            LOGGER.trace("NgoCreateRequestDto={}", ngoCreateRequestDto);
            UUID userId = getCurrentUserId();
            validateUserHasNgo(userId);
            NgoEntity ngoEntity = buildNgoEntity(ngoCreateRequestDto, userId);
            List<NgoDocumentEntity> ngoDocuments = buildNgoDocuments(ngoCreateRequestDto.getDocumentsId(), ngoEntity, userId);
            List<NgoImageEntity> ngoImages = buildNgoImages(ngoCreateRequestDto.getImages(), ngoEntity, userId);
            ngoEntity.setNgoDocuments(ngoDocuments);
            ngoEntity.setNgoImages(ngoImages);
            LOGGER.trace("NgoEntity={}", ngoEntity);
            ngoRepository.save(ngoEntity);
            addRolNgoToUser(userId);
            LOGGER.info("NGO with ID {} successfully registered by user {}", ngoEntity.getId(), userId);
        } catch (DataAccessException ex) {
            LOGGER.error("Database error occurred while registering NGO: {}", ex.getMessage());
            throw new CustomException("An error occurred when try save NGO", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    private UUID getCurrentUserId() {
        UUID userId =  contextService.getUserId();
        if (userId == null) {
            LOGGER.warn("User ID not found in context");
            throw new CustomException("User ID not found in context", HttpStatus.UNAUTHORIZED);
        }
        return userId;
    }

    private void validateUserHasNgo(UUID userId) {
        if (ngoRepository.existsByUserIdCreatorId(userId)) {
            LOGGER.warn("User with ID {} already has an NGO", userId);
            throw new CustomException("User already has an NGO", HttpStatus.BAD_REQUEST);
        }
    }

    private void addRolNgoToUser(UUID userId) {
        updateUserService.addRoleToUser(userId, UserRole.ORGANIZATION);
    }

    private NgoEntity buildNgoEntity(NgoCreateRequestDto ngoCreateRequestDto, UUID userId) {
        UserEntity userEntity = UserEntity.builder().id(userId).build();
        return NgoEntity.builder()
                .id(UUID.randomUUID())
                .name(ngoCreateRequestDto.getName())
                .description(ngoCreateRequestDto.getDescription())
                .profileFileId(uuidConversion(ngoCreateRequestDto.getProfileFileId(), "profileFileId"))
                .bannerFileId(uuidConversion(ngoCreateRequestDto.getBannerFileId(), "bannerFileId"))
                .userIdCreator(userEntity)
                .ngoDocuments(new ArrayList<>())
                .ngoImages(new ArrayList<>())
                .verificationStatus(NgoStatus.PENDING)
                .createdUser(userId)
                .lastUpdatedUser(userId)
                .build();
    }

    private List<NgoDocumentEntity> buildNgoDocuments(List<String> documentsId, NgoEntity ngoEntity, UUID userId) {
        List<NgoDocumentEntity> ngoDocuments = new ArrayList<>();
        if (documentsId != null) {
            for (String docId : documentsId) {
                UUID documentUuid = uuidConversion(docId, "documentId");
                NgoDocumentEntity ngoDocument = NgoDocumentEntity.builder()
                        .id(UUID.randomUUID())
                        .ngo(ngoEntity)
                        .fileId(documentUuid)
                        .status(NgoDocumentStatus.RECEIVED)
                        .createdUser(userId)
                        .lastUpdatedUser(userId)
                        .build();
                ngoDocuments.add(ngoDocument);
            }
        }
        return ngoDocuments;
    }

    private List<NgoImageEntity> buildNgoImages(List<NgoImageDto> images, NgoEntity ngoEntity, UUID userId) {
        List<NgoImageEntity> ngoImages = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (NgoImageDto img : images) {
                UUID imageUuid = uuidConversion(img.getImageId(), "imageId");
                NgoImageEntity ngoImage = NgoImageEntity.builder()
                        .id(UUID.randomUUID())
                        .ngo(ngoEntity)
                        .fileId(imageUuid)
                        .orderIndex(img.getOrderIndex())
                        .createdUser(userId)
                        .lastUpdatedUser(userId)
                        .build();
                ngoImages.add(ngoImage);
            }
        }
        return ngoImages;
    }

    private UUID uuidConversion(String id, String fieldName) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid UUID format for {}: {}", fieldName, id);
            throw new CustomException("Invalid UUID format for " + fieldName, HttpStatus.BAD_REQUEST, e);
        }
    }
}
