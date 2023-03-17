CREATE TABLE users (
                       username VARCHAR(50),
                       password VARCHAR(50),
                       email VARCHAR(50),
                       CONSTRAINT username_unique UNIQUE(username)
);