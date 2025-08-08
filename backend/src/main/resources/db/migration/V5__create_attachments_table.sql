CREATE TABLE attachments (
    id SERIAL PRIMARY KEY,
    file_base64 TEXT NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);