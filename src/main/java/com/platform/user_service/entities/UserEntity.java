package com.platform.user_service.entities;

import com.platform.user_service.enums.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing a user in the system.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email"))
public class UserEntity extends AuditEntity {
    /**
     * Unique identifier for the user.
     */
    @Id
    @Column(name = "user_id", nullable = false, length = 36)
    private UUID id;
    /**
     * First name of the user.
     */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    /**
     * Last name of the user.
      */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    /**
     * Email address of the user.
     */
    @Column(name = "email", nullable = false, length = 255)
    private String email;
    /**
     * Image profile.
     */
    @Column(name = "profile_file_id", length = 36)
    private UUID profileFileId; // handled by media-service
    /**
     * Status of the user account.
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status; // active, inactive, blocked
    /**
     * Roles assigned to the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoleEntity> userRoles;
}
