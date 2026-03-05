--liquibase formatted sql
--changeset andri:002-create-users-and-roles

-- Create users table, compatible with Spring Security's UserDetails
CREATE TABLE users (
  id                    BIGSERIAL    PRIMARY KEY,
  public_id             UUID         NOT NULL DEFAULT gen_random_uuid() UNIQUE,
  username              VARCHAR(100) NOT NULL UNIQUE,
  email                 VARCHAR(255) NOT NULL UNIQUE,
  password              VARCHAR(255) NOT NULL,
  first_name            VARCHAR(100),
  last_name             VARCHAR(100),
  enabled               BOOLEAN      NOT NULL DEFAULT TRUE,
  locked                BOOLEAN      NOT NULL DEFAULT FALSE,
  credentials_expired   BOOLEAN      NOT NULL DEFAULT FALSE,
  account_expired       BOOLEAN      NOT NULL DEFAULT FALSE,
  last_login_at         TIMESTAMPTZ,
  failed_login_attempts INT          NOT NULL DEFAULT 0,
  created_at            TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at            TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  created_by            BIGINT       REFERENCES users(id), 
  updated_by            BIGINT       REFERENCES users(id),
  version               INT          NOT NULL DEFAULT 0 
);

-- Create a table for global system roles
-- Project-scoped roles will be implemented later
CREATE TABLE roles (
  id   SERIAL      PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

-- Seed role data
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

-- Join table for roles for possibility of multiple roles
CREATE TABLE user_roles (
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  role_id INT    NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
  PRIMARY KEY (user_id, role_id)
);

--changeset andri:002-create-updated-at-trigger splitStatements:false

-- Reusable trigger function for updating updated_at on any table
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- User table trigger for updated_at
CREATE TRIGGER trg_users_updated_at
  BEFORE UPDATE ON users
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();