package com.platform.user_service.repositories;

import com.platform.user_service.entities.NgoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * Repository interface for managing NgoEntity entities.
 */
@Repository
public interface NgoRepository extends JpaRepository<NgoEntity, UUID> {
    /**
     * Checks if an NGO exists for a given user ID.
     *
     * @param userIdCreatorId The ID of the user who created the NGO.
     * @return true if an NGO exists for the given user ID, false otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END FROM NgoEntity n "
            + "WHERE n.userIdCreator.id = :userIdCreatorId and n.enabled = true")
    boolean existsByUserIdCreatorId(UUID userIdCreatorId);
    /**
     * Retrieves all NGOs with a 'PENDING' verification status.
     * This query fetches associated documents, images, and the creator user,
     * ensuring that only enabled entities are included.
     *
     * @return a list of NgoEntity objects with 'PENDING' status
     */
    @Query("SELECT distinct n FROM NgoEntity n "
            + "JOIN FETCH n.ngoDocuments d "
            + "LEFT JOIN FETCH n.ngoImages i "
            + "JOIN FETCH n.userIdCreator u "
            + "WHERE n.verificationStatus = 'PENDING' and n.enabled = true and u.enabled = true "
            + "and d.enabled = true "
            + "and (i.enabled = true or i.enabled is null)")
    List<NgoEntity> findAllPending();
    /**
     * Finds an enabled NGO by its ID.
     *
     * @param id the ID of the NGO
     * @return an Optional containing the NgoEntity if found and enabled,
     * or empty if not found or disabled
     */
    Optional<NgoEntity> findByIdAndEnabledTrue(UUID id);
    /**
     * Retrieves a full NgoEntity by its ID, including associated documents, images, and the creator user.
     * This query uses JOIN FETCH to eagerly load related entities.
     *
     * @param id the ID of the NGO
     * @return an Optional containing the full NgoEntity if found, or empty if not found
     */
    @Query("SELECT n FROM NgoEntity n "
            + "JOIN FETCH n.ngoDocuments d "
            + "LEFT JOIN FETCH n.ngoImages i "
            + "JOIN FETCH n.userIdCreator u "
            + " WHERE n.id = :id and n.enabled = true")
    Optional<NgoEntity> findByIdFull(UUID id);
}
