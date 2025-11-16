package com.platform.user_service.repositories;

import com.platform.user_service.entities.UserEntity;
import com.platform.user_service.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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
    /**
     * Finds a user by ID if the user is enabled.
     * Includes fetching associated user roles.
     *
     * @param id the unique identifier of the user
     * @return an Optional containing the UserEntity
     * if found and enabled, otherwise empty
     */
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.userRoles r WHERE u.id = :id")
    Optional<UserEntity> findByIdAndEnabledIsTrueWithRoles(UUID id);

    /**
     * Finds all users with the specified role.
     * Includes fetching associated user roles.
     *
     * @param role the user role to filter by
     * @return a list of UserEntity objects with the specified role
     */
    @Query("""
       SELECT DISTINCT u
       FROM UserEntity u
       JOIN FETCH u.userRoles r
       WHERE r.id.rol = :role
       """)
    List<UserEntity> findAllByUserRolesIs(@Param("role") UserRole role);

    //Todo: enable when save in mysql db. In h2 db,
    // the user save always as enabled = false
    // @Query("SELECT u FROM UserEntity u JOIN FETCH u.userRoles r
    // WHERE u.id = :id AND u.enabled = true")

}
