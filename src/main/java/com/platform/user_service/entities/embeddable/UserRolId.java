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

    @Column(name = "user_id", nullable = false, length = 36)
    private UUID userId;

    @Column(name = "rol", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole rol;

    public UserRolId() {}

    public UserRolId(UUID userId, UserRole rol) {
        this.userId = userId;
        this.rol = rol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRolId that)) return false;
        return Objects.equals(userId, that.userId) && rol == that.rol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, rol);
    }
}
