CREATE TABLE IF NOT EXISTS "USER" (
    "ID" VARCHAR(255) PRIMARY KEY,
    "USERNAME" VARCHAR(255) NOT NULL,
    "PASSWORD" VARCHAR(255) NOT NULL,
    "ROLE" VARCHAR(255) NOT NULL
);