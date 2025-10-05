package com.platform.user_service.entities.embeddable;

import com.platform.user_service.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Composite primary key for UserRolEntity (user_id + rol).
 */
@Data
@Embeddable
public class UserRolId implements Serializable {
    /**
     * Serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the user.
     */
    @Column(name = "user_id", nullable = false, length = 36)
    private UUID userId;

    /**
     * Role assigned to the user.
     */
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole rol;

    /**
     * Default constructor.
     */
    public UserRolId() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    /**
     * Parameterized constructor.
     *
     * @param userIdParam ID of the user.
     * @param roleParam   Role assigned to the user.
     */
    public UserRolId(UUID userIdParam, UserRole roleParam) {
        this.userId = userIdParam;
        this.rol = roleParam;
    }

    /**
     * Override equals for proper comparison of composite key.
     *
     * @param o Object to compare.
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRolId that)) {
            return false;
        }
        return Objects.equals(userId, that.userId) && rol == that.rol;
    }
    /**
     * Override hashCode for proper hashing of composite key.
     *
     * @return hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, rol);
    }
}
