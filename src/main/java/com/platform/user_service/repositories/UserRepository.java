package com.platform.user_service.repositories;

import com.platform.user_service.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing UserEntity entities.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    /**
     * Checks if a user with the given email exists.
     *
     * @param email the email to check
     * @return true if a user with the given email exists, otherwise false
     */
    boolean existsByEmail(String email);
}
