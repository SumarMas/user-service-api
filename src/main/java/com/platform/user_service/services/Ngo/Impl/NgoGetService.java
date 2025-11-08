package com.platform.user_service.services.Ngo.Impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.common.NgoDto;
import com.platform.user_service.dtos.common.NgoImageDto;
import com.platform.user_service.dtos.common.UserDto;
import com.platform.user_service.entities.AuditEntity;
import com.platform.user_service.entities.NgoEntity;
import com.platform.user_service.entities.NgoImageEntity;
import com.platform.user_service.entities.UserEntity;
import com.platform.user_service.enums.NgoStatus;
import com.platform.user_service.repositories.NgoRepository;
import com.platform.user_service.services.user.IContextService;
import com.platform.user_service.services.Ngo.INgoGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
/**
 * Service implementation for handling NGO retrieval operations.
 */
@Service
@RequiredArgsConstructor
public class NgoGetService implements INgoGetService {
    /** Logger for logging information and errors. */
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(NgoGetService.class);
    /** Repository for accessing NGO data. */
    private final NgoRepository ngoRepository;
    /** Service for managing context-related operations. */
    private final IContextService contextService;
    /**
     * Retrieves a list of all NGOs approved.
     *
     * @return a list of NgoDto representing all NGOs
     */
    @Override
    public List<NgoDto> getAllApproveNgos() {
        LOG.trace("In NgoGetService.getAllApproveNgos()");
        List<NgoEntity> ngos = findAllNgosEnable().stream()
                .filter(ngo -> ngo.getVerificationStatus().equals(NgoStatus.VERIFIED)).toList();
        return ngos.stream().map(this::mapNgoPendingDto).toList();
    }

    /**
     * Retrieves the details of a specific NGO by its ID.
     *
     * @param ngoId the unique identifier of the NGO
     * @return the NgoDto representing the NGO details
     */
    @Override
    public NgoDto getNgoById(UUID ngoId) {
        LOG.trace("getNgoById: {}", ngoId);
        NgoEntity ngoEntity = findNgoById(ngoId);
        return mapNgoPendingDto(ngoEntity);
    }

    /**
     * Retrieves the NGO associated with the currently authenticated user.
     *
     * @return the NgoDto representing the user's NGO details
     */
    @Override
    public NgoDto getMyNgo() {
        LOG.trace("getMyNgo");
        UUID userId = getCurrentUserId();
        NgoEntity ngoEntity = findNgoByUserIdCreator(userId);
        return mapNgoPendingDto(ngoEntity);
    }

    /**
     * Retrieves a list of NGOs pending approval.
     *
     * @return a list of NgoDto representing the pending NGOs
     */
    @Override
    public List<NgoDto> getPendingNgos() {
        LOG.trace("Retrieving pending NGOs");
        checkAdmin();
        List<NgoEntity> ngos = findAllPendingNgos();
        return ngos.stream().map(this::mapNgoPendingDto).toList();
    }

    /**
     * Retrieves a list of all NGOs.
     *
     * @return a list of NgoDto representing all NGOs
     */
    @Override
    public List<NgoDto> getAllNgos() {
        LOG.trace("getAllNgos");
        List<NgoEntity> ngos = findAllNgos();
        return ngos.stream().map(this::mapNgoPendingDto).toList();
    }

    private void checkAdmin() {
        if (!contextService.isAdmin()) {
            LOG.warn("User is not admin");
            throw new CustomException("User is unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
    private UUID getCurrentUserId() {
        UUID currentUserId = contextService.getUserId();
        if (currentUserId == null) {
            LOG.warn("Current user id is null");
            throw new CustomException("User is unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return currentUserId;
    }
    private List<NgoEntity> findAllPendingNgos() {
        try {
            return ngoRepository.findAllPending();
        } catch (DataAccessException ex) {
            LOG.error("Database access error while retrieving pending NGOs: {}", ex.getMessage());
            return List.of();
        }
    }
    private List<NgoEntity> findAllNgosEnable() {
        try {
            return ngoRepository.findAllByEnabledIsTrue();
        } catch (DataAccessException ex) {
            LOG.error("Database access error while retrieving all NGOs Enable: {}", ex.getMessage());
            return List.of();
        }
    }
    private List<NgoEntity> findAllNgos() {
        try {
            return ngoRepository.findAll();
        } catch (DataAccessException ex) {
            LOG.error("Database access error while retrieving all NGOs: {}", ex.getMessage());
            return List.of();
        }
    }
    private NgoEntity findNgoById(UUID ngoId) {
        try {
            return ngoRepository.findByIdAndEnabledTrue(ngoId)
                    .orElseThrow(() -> new CustomException("NGO not found", HttpStatus.NOT_FOUND));
        } catch (DataAccessException ex) {
            LOG.error("Database access error while retrieving NGO by ID: {}", ex.getMessage());
            throw new CustomException("An error occurred while obtaining the information. ",
                    HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }
    private NgoEntity findNgoByUserIdCreator(UUID userIdCreatorId) {
        try {
            return ngoRepository.findByUserIdCreatorId(userIdCreatorId)
                    .orElseThrow(() -> new CustomException("NGO not found for the user", HttpStatus.NOT_FOUND));
        } catch (DataAccessException ex) {
            LOG.error("Database access error while retrieving NGO by user ID: {}", ex.getMessage());
            throw new CustomException("An error occurred while obtaining the information.",
                    HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }
    private NgoDto mapNgoPendingDto(NgoEntity ngoEntity) {
        if (ngoEntity == null) {
            return null;
        }
        if (ngoEntity.getNgoDocuments() == null) {
            ngoEntity.setNgoDocuments(List.of());
        }
        if (ngoEntity.getNgoImages() == null) {
            ngoEntity.setNgoImages(List.of());
        }
        return NgoDto.builder()
                .id(ngoEntity.getId().toString())
                .name(ngoEntity.getName())
                .description(ngoEntity.getDescription())
                .bannerFileId(ngoEntity.getBannerFileId().toString())
                .profileFileId(ngoEntity.getProfileFileId().toString())
                .createdDateTime(ngoEntity.getCreatedDatetime())
                .userCreator(mapUserCreator(ngoEntity.getUserIdCreator()))
                .documentsId(ngoEntity.getNgoDocuments().stream()
                        .filter(AuditEntity::getEnabled)
                        .map(doc -> doc.getFileId().toString()).toList())
                .images(ngoEntity.getNgoImages().stream()
                        .filter(AuditEntity::getEnabled)
                        .map(this::mapNgoImageDto).toList())
                .status(ngoEntity.getVerificationStatus().name())
                .build();
    }
    private UserDto mapUserCreator(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId().toString())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .roles(userEntity.getUserRoles().stream().map(rol -> rol.getRol().name()).toList())
                .profileFileId(userEntity.getProfileFileId() != null ? userEntity.getProfileFileId().toString() : null)
                .status(userEntity.getStatus().name())
                .build();
    }
    private NgoImageDto mapNgoImageDto(NgoImageEntity ngoImageEntity) {
        return NgoImageDto.builder()
                .imageId(ngoImageEntity.getFileId().toString())
                .orderIndex(ngoImageEntity.getOrderIndex())
                .build();
    }
}
