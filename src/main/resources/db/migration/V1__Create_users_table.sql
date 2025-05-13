CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL DEFAULT 'USER',
                       is_active BOOLEAN NOT NULL DEFAULT true,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_active ON users(is_active);

-- Create an admin user (password should be changed after initial setup)
INSERT INTO users (email, password, first_name, last_name, role, is_active)
VALUES ('garvey@pricesurvey.com', '$2a$10$XPtYPpZGcj.w8B8VQ4yBx.RjFYUEqBY3vY4ZfF7RQyYZB5Q6Wq7Kq', 'Garvey', 'Admin', 'ADMIN', true)
    ON CONFLICT (email) DO NOTHING;