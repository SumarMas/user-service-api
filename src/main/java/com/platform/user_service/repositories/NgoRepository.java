package com.platform.user_service.repositories;

import com.platform.user_service.entities.NgoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
