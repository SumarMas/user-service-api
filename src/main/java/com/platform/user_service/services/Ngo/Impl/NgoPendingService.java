package com.platform.user_service.services.Ngo.Impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.common.NgoImageDto;
import com.platform.user_service.dtos.common.UserDto;
import com.platform.user_service.dtos.response.NgoPendingDto;
import com.platform.user_service.entities.NgoEntity;
import com.platform.user_service.entities.NgoImageEntity;
import com.platform.user_service.entities.UserEntity;
import com.platform.user_service.entities.UserRoleEntity;
import com.platform.user_service.repositories.NgoRepository;
import com.platform.user_service.services.IContextService;
import com.platform.user_service.services.Ngo.INgoPendingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service implementation for handling pending validation NGOs.
 */
@Service
@RequiredArgsConstructor
public class NgoPendingService implements INgoPendingService {
    /** Logger for logging information and errors. */
    private static final Logger LOG = LoggerFactory.getLogger(NgoPendingService.class);
    /** Repository for accessing NGO data. */
    private final NgoRepository ngoRepository;
    /** Service for managing context-related operations. */
    private final IContextService contextService;
    /**
     * Retrieves a list of pending validation NGOs.
     *
     * @return a list of NgoPendingDto representing the pending NGOs
     */
    @Override
    public List<NgoPendingDto> getPending() {
        checkAdmin();
        return getAllPending();
    }

    private void checkAdmin() {
        if (!contextService.isAdmin()) {
            LOG.error("User is not admin");
            throw new CustomException("User is unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
    private List<NgoPendingDto> getAllPending() {
        List<NgoEntity> pendingEntity = ngoRepository.findAllPending();
        return pendingEntity.stream().map(ngo -> NgoPendingDto.builder()
                .id(ngo.getId().toString())
                .name(ngo.getName())
                .description(ngo.getDescription())
                .bannerFileId(ngo.getBannerFileId().toString())
                .profileFileId(ngo.getProfileFileId().toString())
                .createdDateTime(ngo.getCreatedDatetime())
                .userCreator(mapUserCreator(ngo.getUserIdCreator()))
                .documentsId(ngo.getNgoDocuments().stream().map(doc -> doc.getFileId().toString()).toList())
                .images(ngo.getNgoImages().stream().map(this::mapNgoImageDto).toList())
                .build()).toList();
    }

    private UserDto mapUserCreator(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId().toString())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .roles(userEntity.getUserRoles().stream().map(rol -> rol.getRol().name()).toList())
                .profileFileId(userEntity.getProfileFileId().toString())
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
