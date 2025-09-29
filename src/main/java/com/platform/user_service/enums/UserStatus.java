package com.platform.user_service.enums;

/**
 * Enum representing the status of a user account.
 */
public enum UserStatus {
    /**
     * Status when the user account is active and in good standing.
     */
    ACTIVE,
    /**
     * Status when the user account is inactive,
     * possibly due to inactivity or user choice.
     */
    INACTIVE,
    /**
     * Status when the user account is blocked
     * due to violations of terms or suspicious activity.
     */
    BLOCKED
}
