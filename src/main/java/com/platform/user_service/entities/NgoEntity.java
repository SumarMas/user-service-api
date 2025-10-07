package com.platform.user_service.entities;

import com.platform.user_service.enums.NgoStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing a Non-Governmental Organization (NGO).
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "ngos")
public class NgoEntity extends AuditEntity {
    /**
     * Unique identifier for the NGO.
     */
    @Id
    @Column(name = "ngo_id", nullable = false, length = 36)
    private UUID id;

    /**
     * The user who created the NGO entry.
     */
    @OneToOne
    @JoinColumn(name = "user_id_creator", nullable = false)
    private UserEntity userIdCreator;

    /**
     * Name of the NGO.
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Description of the NGO.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Image profile.
     */
    @Column(name = "profile_file_id", length = 36)
    private UUID profileFileId; // handled by media-service
    /**
     * Image banner.
     */
    @Column(name = "banner_file_id", length = 36)
    private UUID bannerFileId; // handled by media-service

    /**
     * Verification status of the NGO.
     */
    @Column(name = "verification_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private NgoStatus verificationStatus; // UNVERIFIED, PENDING, VERIFIED, DENIED

    /**
     * Documents submitted by the NGO for validation.
     */
    @OneToMany(mappedBy = "ngo", cascade = CascadeType.ALL)
    private List<NgoDocumentEntity> ngoDocuments;

    /**
     * Images associated with the NGO.
     */
    @OneToMany(mappedBy = "ngo", cascade = CascadeType.ALL)
    private List<NgoImageEntity> ngoImages;
}
