package com.platform.user_service.entities;

import com.platform.user_service.entities.embeddable.UserRolId;
import com.platform.user_service.enums.UserRole;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing the many-to-many relationship between users and roles.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "user_roles")
public class UserRoleEntity extends AuditEntity {
    /**
     * Composite primary key consisting of user ID and role.
     */
    @EmbeddedId
    private UserRolId id;

    /**
     * The user associated with this role.
     */
    @ManyToOne()
    @MapsId("userId") // enlaza el campo userId de UserRolId
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    /**
     * Gets the role from the composite key.
     *
     * @return The role.
     */
    public UserRole getRol() {
        return id != null ? id.getRol() : null;
    }
    /**
     * Sets the role in the composite key.
     *
     * @param rol The role to set.
     */
    public void setRol(UserRole rol) {
        if (id == null) {
            id = new UserRolId();
        }
        id.setRol(rol);
    }
}
