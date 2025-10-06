-- ================================================
-- USER SERVICE SCHEMA (MySQL adjusted with safety checks)
-- ================================================
CREATE DATABASE IF NOT EXISTS user_service;
USE user_service;

-- =========================================================
-- USERS
-- =========================================================
CREATE TABLE IF NOT EXISTS users (
                       user_id BINARY(16) PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       profile_file_id BINARY(16),
                       status VARCHAR(20) NOT NULL,
                       created_datetime DATETIME NOT NULL,
                       created_user BINARY(16) NOT NULL,
                       last_updated_datetime DATETIME NOT NULL,
                       last_updated_user BINARY(16) NOT NULL,
                       enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS users_audit (
                             user_id BINARY(16) NOT NULL,
                             version INT NOT NULL,
                             first_name VARCHAR(100),
                             last_name VARCHAR(100),
                             email VARCHAR(255),
                             profile_file_id BINARY(16),
                             status VARCHAR(20),
                             created_datetime DATETIME,
                             created_user BINARY(16),
                             last_updated_datetime DATETIME,
                             last_updated_user BINARY(16),
                             enabled BOOLEAN,
                             PRIMARY KEY (user_id , version)
);

-- =========================================================
-- USER_ROLES
-- =========================================================
CREATE TABLE IF NOT EXISTS user_roles (
                            user_id BINARY(16) NOT NULL,
                            role VARCHAR(50) NOT NULL,
                            created_datetime DATETIME NOT NULL,
                            created_user BINARY(16) NOT NULL,
                            last_updated_datetime DATETIME NOT NULL,
                            last_updated_user BINARY(16) NOT NULL,
                            enabled BOOLEAN DEFAULT TRUE,
                            PRIMARY KEY (user_id , role),
                            FOREIGN KEY (user_id)
                                REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS user_roles_audit (
                                  user_id BINARY(16) NOT NULL,
                                  role VARCHAR(50) NOT NULL,
                                  version INT NOT NULL,
                                  created_datetime DATETIME,
                                  created_user BINARY(16),
                                  last_updated_datetime DATETIME,
                                  last_updated_user BINARY(16),
                                  enabled BOOLEAN,
                                  PRIMARY KEY (user_id , role , version)
);

-- =========================================================
-- NGOS
-- =========================================================
CREATE TABLE IF NOT EXISTS ngos (
                      ngo_id BINARY(16) PRIMARY KEY,
                      user_id_creator BINARY(16) NOT NULL,
                      name VARCHAR(255) NOT NULL,
                      description TEXT,
                      profile_file_id BINARY(16),
                      banner_file_id BINARY(16),
                      verification_status VARCHAR(20) NOT NULL,
                      created_datetime DATETIME NOT NULL,
                      created_user BINARY(16) NOT NULL,
                      last_updated_datetime DATETIME NOT NULL,
                      last_updated_user BINARY(16) NOT NULL,
                      enabled BOOLEAN DEFAULT TRUE,
                      FOREIGN KEY (user_id_creator)
                          REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS ngos_audit (
                            ngo_id BINARY(16) NOT NULL,
                            version INT NOT NULL,
                            user_id_creator BINARY(16),
                            name VARCHAR(255),
                            description TEXT,
                            profile_file_id BINARY(16),
                            banner_file_id BINARY(16),
                            verification_status VARCHAR(20),
                            created_datetime DATETIME,
                            created_user BINARY(16),
                            last_updated_datetime DATETIME,
                            last_updated_user BINARY(16),
                            enabled BOOLEAN,
                            PRIMARY KEY (ngo_id , version)
);
-- =========================================================
-- NGO_IMAGES
-- =========================================================
CREATE TABLE IF NOT EXISTS ngo_images (
                            ngo_image_id BINARY(16) NOT NULL,
                            ngo_id BINARY(16) NOT NULL,
                            file_id BINARY(16) NOT NULL,
                            order_index INT default 1,
                            created_datetime DATETIME,
                            created_user BINARY(16),
                            last_updated_datetime DATETIME,
                            last_updated_user BINARY(16),
                            enabled BOOLEAN,
                            PRIMARY KEY (ngo_image_id),
                            CONSTRAINT fk_ngo_images_ngo FOREIGN KEY (ngo_id)
                                REFERENCES ngos (ngo_id)
);

CREATE TABLE IF NOT EXISTS ngo_images_audit (
    ngo_image_id BINARY(16) NOT NULL,
    version INT NOT NULL,
    ngo_id BINARY(16) NOT NULL,
    file_id BINARY(16) NOT NULL,
    order_index INT,
    created_datetime DATETIME,
    created_user BINARY(16),
    last_updated_datetime DATETIME,
    last_updated_user BINARY(16),
    enabled BOOLEAN,
    PRIMARY KEY (ngo_image_id, version)
    );

-- =========================================================
-- NGO_DOCUMENTS
-- =========================================================
CREATE TABLE IF NOT EXISTS ngo_documents (
                               document_id BINARY(16) PRIMARY KEY,
                               ngo_id BINARY(16) NOT NULL,
                               file_id BINARY(16) NOT NULL,
                               status VARCHAR(20) NOT NULL,
                               admin_comment TEXT,
                               created_datetime DATETIME NOT NULL,
                               created_user BINARY(16) NOT NULL,
                               last_updated_datetime DATETIME NOT NULL,
                               last_updated_user BINARY(16) NOT NULL,
                               enabled BOOLEAN DEFAULT TRUE,
                               FOREIGN KEY (ngo_id)
                                   REFERENCES ngos (ngo_id)
);

CREATE TABLE IF NOT EXISTS ngo_documents_audit (
                                     document_id BINARY(16) NOT NULL,
                                     version INT NOT NULL,
                                     ngo_id BINARY(16),
                                     file_id BINARY(16),
                                     status VARCHAR(20),
                                     admin_comment TEXT,
                                     created_datetime DATETIME,
                                     created_user BINARY(16),
                                     last_updated_datetime DATETIME,
                                     last_updated_user BINARY(16),
                                     enabled BOOLEAN,
                                     PRIMARY KEY (document_id , version)
);

-- =========================================================
-- TRIGGERS
-- =========================================================
DELIMITER $$

-- Users
CREATE TRIGGER before_insert_users
    BEFORE INSERT
    ON users
    FOR EACH ROW
BEGIN
    SET NEW.created_datetime = NOW();
SET NEW.last_updated_datetime = NOW();
SET NEW.enabled = TRUE;
END$$

    CREATE TRIGGER after_insert_users
        AFTER INSERT
        ON users
        FOR EACH ROW
    BEGIN
        INSERT INTO users_audit
        VALUES (NEW.user_id, 1, NEW.first_name, NEW.last_name, NEW.email,
                NEW.profile_file_id, NEW.status,
                NEW.created_datetime, NEW.created_user,
                NEW.last_updated_datetime, NEW.last_updated_user,
                NEW.enabled);
        END$$

        CREATE TRIGGER before_update_users
            BEFORE UPDATE
            ON users
            FOR EACH ROW
        BEGIN
            DECLARE last_version INT;
SET NEW.last_updated_datetime = NOW();
SET NEW.created_user = OLD.created_user;

            SELECT MAX(version) INTO last_version FROM users_audit WHERE user_id = NEW.user_id;

            INSERT INTO users_audit
            VALUES (NEW.user_id, last_version + 1, NEW.first_name, NEW.last_name, NEW.email,
                    NEW.profile_file_id, NEW.status,
                    NEW.created_datetime, NEW.created_user,
                    NEW.last_updated_datetime, NEW.last_updated_user,
                    NEW.enabled);
            END$$

            -- User Roles
            CREATE TRIGGER before_insert_user_roles
                BEFORE INSERT
                ON user_roles
                FOR EACH ROW
            BEGIN
                SET NEW.created_datetime = NOW();
SET NEW.last_updated_datetime = NOW();
SET NEW.enabled = TRUE;
END$$

                CREATE TRIGGER after_insert_user_roles
                    AFTER INSERT
                    ON user_roles
                    FOR EACH ROW
                BEGIN
                    INSERT INTO user_roles_audit
                    VALUES (NEW.user_id, NEW.role, 1,
                            NEW.created_datetime, NEW.created_user,
                            NEW.last_updated_datetime, NEW.last_updated_user,
                            NEW.enabled);
                    END$$

                    CREATE TRIGGER before_update_user_roles
                        BEFORE UPDATE
                        ON user_roles
                        FOR EACH ROW
                    BEGIN
                        DECLARE last_version INT;
SET NEW.last_updated_datetime = NOW();
SET NEW.created_user = OLD.created_user;

                        SELECT MAX(version)
                        INTO last_version
                        FROM user_roles_audit
                        WHERE user_id = NEW.user_id
                          AND role = NEW.role;

                        INSERT INTO user_roles_audit
                        VALUES (NEW.user_id, NEW.role, last_version + 1,
                                NEW.created_datetime, NEW.created_user,
                                NEW.last_updated_datetime, NEW.last_updated_user,
                                NEW.enabled);
                        END$$

                        -- NGOs
                        CREATE TRIGGER before_insert_ngos
                            BEFORE INSERT
                            ON ngos
                            FOR EACH ROW
                        BEGIN
                            SET NEW.created_datetime = NOW();
SET NEW.last_updated_datetime = NOW();
SET NEW.enabled = TRUE;
END$$

                            CREATE TRIGGER after_insert_ngos
                                AFTER INSERT
                                ON ngos
                                FOR EACH ROW
                            BEGIN
                                INSERT INTO ngos_audit
                                VALUES (NEW.ngo_id, 1, NEW.user_id_creator,
                                        NEW.name, NEW.description, NEW.profile_file_id,
                                        NEW.banner_file_id, NEW.verification_status,
                                        NEW.created_datetime, NEW.created_user,
                                        NEW.last_updated_datetime, NEW.last_updated_user,
                                        NEW.enabled);
                                END$$

                                CREATE TRIGGER before_update_ngos
                                    BEFORE UPDATE
                                    ON ngos
                                    FOR EACH ROW
                                BEGIN
                                    DECLARE last_version INT;
SET NEW.last_updated_datetime = NOW();
SET NEW.created_user = OLD.created_user;

                                    SELECT MAX(version) INTO last_version FROM ngos_audit WHERE ngo_id = NEW.ngo_id;

                                    INSERT INTO ngos_audit
                                    VALUES (NEW.ngo_id, last_version + 1, NEW.user_id_creator,
                                            NEW.name, NEW.description, NEW.profile_file_id,
                                            NEW.banner_file_id, NEW.verification_status,
                                            NEW.created_datetime, NEW.created_user,
                                            NEW.last_updated_datetime, NEW.last_updated_user,
                                            NEW.enabled);
                                    END$$
-- NGO Images
-- BEFORE INSERT
CREATE TRIGGER before_insert_ngo_images
BEFORE INSERT
ON ngo_images
FOR EACH ROW
BEGIN
SET NEW.created_datetime = NOW();
SET NEW.last_updated_datetime = NOW();
SET NEW.enabled = TRUE;
END$$


-- AFTER INSERT
CREATE TRIGGER after_insert_ngo_images
AFTER INSERT
ON ngo_images
FOR EACH ROW
BEGIN
INSERT INTO ngo_images_audit
VALUES (
           NEW.ngo_image_id, 1, NEW.ngo_id,
           NEW.file_id, NEW.order_index,
           NEW.created_datetime, NEW.created_user,
           NEW.last_updated_datetime, NEW.last_updated_user,
           NEW.enabled
       );
END$$


-- BEFORE UPDATE
CREATE TRIGGER before_update_ngo_images
    BEFORE UPDATE
    ON ngo_images
    FOR EACH ROW
BEGIN
    DECLARE last_version INT;

SET NEW.last_updated_datetime = NOW();
SET NEW.created_user = OLD.created_user;

    SELECT MAX(version)
    INTO last_version
    FROM ngo_images_audit
    WHERE ngo_image_id = NEW.ngo_image_id;

    IF last_version IS NULL THEN
SET last_version = 0;
END IF;

INSERT INTO ngo_images_audit
VALUES (
           NEW.ngo_image_id, last_version + 1, NEW.ngo_id,
           NEW.file_id, NEW.order_index,
           NEW.created_datetime, NEW.created_user,
           NEW.last_updated_datetime, NEW.last_updated_user,
           NEW.enabled
       );
END$$

-- NGO Documents
CREATE TRIGGER before_insert_ngo_documents
BEFORE INSERT
ON ngo_documents
FOR EACH ROW
BEGIN
SET NEW.created_datetime = NOW();
SET NEW.last_updated_datetime = NOW();
SET NEW.enabled = TRUE;
END$$

CREATE TRIGGER after_insert_ngo_documents
AFTER INSERT
ON ngo_documents
FOR EACH ROW
BEGIN
INSERT INTO ngo_documents_audit
VALUES (NEW.document_id, 1, NEW.ngo_id,
    NEW.file_id, NEW.status, NEW.admin_comment,
    NEW.created_datetime, NEW.created_user,
    NEW.last_updated_datetime, NEW.last_updated_user,
    NEW.enabled);
END$$

CREATE TRIGGER before_update_ngo_documents
BEFORE UPDATE
ON ngo_documents
FOR EACH ROW
BEGIN
DECLARE last_version INT;
SET NEW.last_updated_datetime = NOW();
SET NEW.created_user = OLD.created_user;

SELECT MAX(version)
INTO last_version
FROM ngo_documents_audit
WHERE document_id = NEW.document_id;

INSERT INTO ngo_documents_audit
VALUES (NEW.document_id, last_version + 1, NEW.ngo_id,
        NEW.file_id, NEW.status, NEW.admin_comment,
        NEW.created_datetime, NEW.created_user,
        NEW.last_updated_datetime, NEW.last_updated_user,
        NEW.enabled);
END$$

DELIMITER ;
