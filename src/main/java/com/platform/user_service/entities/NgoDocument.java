package com.platform.user_service.entities;

import com.platform.user_service.enums.NgoDocumentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Entity representing documents submitted by an NGO for validation.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ngo_documents")
public class NgoDocument extends AuditEntity {
    @Id
    @Column(name = "document_id", nullable = false, length = 36)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ngo_id", nullable = false)
    private NgoEntity ngo;

    @Column(name = "file_id", nullable = false, length = 36)
    private UUID fileId; // handled by media-service

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private NgoDocumentStatus status; // received, accepted, rejected

    @Column(name = "admin_comment", columnDefinition = "TEXT")
    private String adminComment;
}
