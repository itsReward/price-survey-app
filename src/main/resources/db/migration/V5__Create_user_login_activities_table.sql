CREATE TABLE user_login_activities (
                                       id BIGSERIAL PRIMARY KEY,
                                       user_id BIGINT NOT NULL,
                                       ip_address VARCHAR(45),
                                       user_agent TEXT,
                                       logged_in_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                       CONSTRAINT fk_user_login_activities_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_user_login_activities_user ON user_login_activities(user_id);
CREATE INDEX idx_user_login_activities_logged_in_at ON user_login_activities(logged_in_at);

-- Create user_stores junction table for many-to-many relationship
CREATE TABLE user_stores (
                             user_id BIGINT NOT NULL,
                             store_id BIGINT NOT NULL,
                             PRIMARY KEY (user_id, store_id),
                             CONSTRAINT fk_user_stores_user FOREIGN KEY (user_id) REFERENCES users(id),
                             CONSTRAINT fk_user_stores_store FOREIGN KEY (store_id) REFERENCES stores(id)
);