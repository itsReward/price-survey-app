CREATE TABLE stores (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        address VARCHAR(500) NOT NULL,
                        city VARCHAR(255) NOT NULL,
                        region VARCHAR(255) NOT NULL,
                        country VARCHAR(255) NOT NULL,
                        latitude DECIMAL(10, 8) NULL,
                        longitude DECIMAL(11, 8) NULL,
                        is_active BOOLEAN NOT NULL DEFAULT true,
                        created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_stores_city ON stores(city);
CREATE INDEX idx_stores_region ON stores(region);
CREATE INDEX idx_stores_active ON stores(is_active);
CREATE UNIQUE INDEX idx_stores_name_address ON stores(name, address);