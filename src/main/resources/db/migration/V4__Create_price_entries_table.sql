CREATE TABLE price_entries (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               store_id BIGINT NOT NULL,
                               product_id BIGINT NOT NULL,
                               price DECIMAL(10, 2) NOT NULL,
                               quantity INTEGER NOT NULL,
                               notes TEXT,
                               created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_price_entries_user FOREIGN KEY (user_id) REFERENCES users(id),
                               CONSTRAINT fk_price_entries_store FOREIGN KEY (store_id) REFERENCES stores(id),
                               CONSTRAINT fk_price_entries_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_price_entries_user ON price_entries(user_id);
CREATE INDEX idx_price_entries_store ON price_entries(store_id);
CREATE INDEX idx_price_entries_product ON price_entries(product_id);
CREATE INDEX idx_price_entries_created_at ON price_entries(created_at);
CREATE INDEX idx_price_entries_price ON price_entries(price);