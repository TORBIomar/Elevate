-- =============================================
-- Elevate DB — Full schema setup
-- Run this after dropping the old database
-- =============================================

DROP DATABASE IF EXISTS elevate_db;
CREATE DATABASE elevate_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE elevate_db;

-- =============================================
-- Table: users  (matches User.java entity)
-- =============================================
CREATE TABLE users (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    email               VARCHAR(255)    NOT NULL,
    password            VARCHAR(255)    NOT NULL,
    first_name          VARCHAR(255)    NOT NULL,
    last_name           VARCHAR(255)    NOT NULL,
    phone_number        VARCHAR(255)    NULL,
    role                VARCHAR(255)    NOT NULL,          -- ENUM stored as STRING: CANDIDATE, RECRUITER, ADMIN
    profile_picture_url VARCHAR(255)    NULL,
    is_active           BIT(1)          NULL DEFAULT 1,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NULL,
    last_login          DATETIME(6)     NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- Table: job_offers  (matches JobOffer.java entity)
-- =============================================
CREATE TABLE job_offers (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    title               VARCHAR(255)    NOT NULL,
    description         TEXT            NOT NULL,
    location            VARCHAR(255)    NOT NULL,
    job_type            VARCHAR(255)    NOT NULL,
    category            VARCHAR(255)    NOT NULL,
    salary              DOUBLE          NOT NULL,
    recruiter_id        BIGINT          NOT NULL,
    created_at          DATETIME(6)     NULL,
    updated_at          DATETIME(6)     NULL,

    PRIMARY KEY (id),
    KEY fk_job_offers_recruiter (recruiter_id),
    CONSTRAINT fk_job_offers_recruiter FOREIGN KEY (recruiter_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
