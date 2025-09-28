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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing a Non-Governmental Organization (NGO).
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ngos")
public class NgoEntity extends AuditEntity {
    @Id
    @Column(name = "ngo_id", nullable = false, length = 36)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id_creator", nullable = false)
    private UserEntity userIdCreator;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "profile_file_id", length = 36)
    private UUID profileFileId; // handled by media-service

    @Column(name = "verification_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private NgoStatus verificationStatus; // UNVERIFIED, PENDING, VERIFIED, DENIED

    @OneToMany(mappedBy = "ngo", cascade = CascadeType.ALL)
    private List<NgoDocument> ngoDocuments;
}
