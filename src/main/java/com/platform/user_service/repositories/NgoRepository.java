package com.platform.user_service.repositories;

import com.platform.user_service.entities.NgoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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
    boolean existsByUserIdCreator_Id(UUID userIdCreatorId);
}
