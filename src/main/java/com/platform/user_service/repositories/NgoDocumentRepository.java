package com.platform.user_service.repositories;

import com.platform.user_service.entities.NgoDocumentEntity;
import com.platform.user_service.enums.NgoDocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing NgoDocumentEntity entities.
 */
@Repository
public interface NgoDocumentRepository extends JpaRepository<NgoDocumentEntity, UUID> {

    /**
     * Finds all NgoDocumentEntity records by NGO ID, status, and enabled flag.
     *
     * @param ngoId  the ID of the NGO
     * @param status the status of the documents
     * @return a list of NgoDocumentEntity records matching the criteria
     */
    @Query("SELECT nde FROM NgoDocumentEntity nde WHERE nde.ngo.id = :ngoId AND "
            + "nde.status = :status AND nde.enabled = true")
    List<NgoDocumentEntity> findAllByNgoIdAndStatusAndEnabled(UUID ngoId, NgoDocumentStatus status);
}
