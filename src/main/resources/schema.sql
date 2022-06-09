CREATE TABLE IF NOT EXISTS user_data (
    id integer NOT NULL,
    username varchar(100) NOT NULL,
    password varchar(100) NOT NULL,
    role varchar(100) NOT NULL,
    PRIMARY KEY (id)
);