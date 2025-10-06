package com.platform.user_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Represents an image associated with an NGO (used for the image carousel).
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "ngo_images")
public class NgoImageEntity extends AuditEntity {

    /**
     * Unique identifier for the NGO image.
     */
    @Id
    @Column(name = "ngo_image_id", nullable = false)
    private UUID id;

    /**
     * The NGO to which this image belongs.
     */
    @ManyToOne
    @JoinColumn(name = "ngo_id", nullable = false)
    private NgoEntity ngo;

    /**
     * The file ID of the image.
     */
    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    /**
     * The order index of the image in the carousel.
     */
    @Column(name = "order_index")
    private Integer orderIndex;
}
