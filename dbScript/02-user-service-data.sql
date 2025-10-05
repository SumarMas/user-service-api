-- =========================================================
-- Initial data for user-service
-- Inserts ADMIN user and assigns ADMIN role
-- =========================================================

-- UUID pre-generated (use same in auth-service credentials)
-- user_id: 11111111-1111-1111-1111-111111111111

-- Insert ADMIN user
use user_service;
INSERT INTO users (user_id, first_name, last_name, email, profile_file_id, status,
                   created_datetime, created_user, last_updated_datetime, last_updated_user, enabled)
VALUES (UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
        'Admin',
        'System',
        'admin@platform.com',
        NULL,
        'ACTIVE',
        NOW(),
        UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
        NOW(),
        UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
        TRUE);

-- Insert ADMIN role
INSERT INTO user_roles (user_id, role,
                        created_datetime, created_user,
                        last_updated_datetime, last_updated_user, enabled)
VALUES (UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
        'ADMIN',
        NOW(),
        UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
        NOW(),
        UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
        TRUE);
