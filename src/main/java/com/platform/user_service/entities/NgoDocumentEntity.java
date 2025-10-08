package com.platform.user_service.entities;

import com.platform.user_service.enums.NgoDocumentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Entity representing documents submitted by an NGO for validation.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "ngo_documents")
public class NgoDocumentEntity extends AuditEntity {
    /**
     * Unique identifier for the NGO document.
     */
    @Id
    @Column(name = "document_id", nullable = false, length = 36)
    private UUID id;
    /**
     * The NGO to which this document belongs.
     */
    @ManyToOne
    @JoinColumn(name = "ngo_id", nullable = false)
    private NgoEntity ngo;
    /**
     * Identifier of the file in the media service.
     */
    @Column(name = "file_id", nullable = false, length = 36)
    private UUID fileId; // handled by media-service
    /**
     * Status of the document validation process.
     */
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private NgoDocumentStatus status; // received, accepted, rejected

    /**
     * Comments from the admin regarding the document status.
     */
    @Column(name = "admin_comment", columnDefinition = "TEXT")
    private String adminComment;
}
