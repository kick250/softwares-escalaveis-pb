CREATE TABLE orders (
    id         SERIAL PRIMARY KEY,
    deleted    BOOLEAN   NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    owner_id   INTEGER   NOT NULL REFERENCES users (id)
);