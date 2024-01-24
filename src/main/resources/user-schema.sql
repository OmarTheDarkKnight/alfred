DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id integer NOT NULL,
    first_name varchar(100) NOT NULL ,
    last_name varchar(100) NOT NULL ,
    email varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
);

INSERT INTO users (id, first_name, last_name, email)
VALUES (1, 'BRUCE', 'WAYNE', 'bruce@waynetech.com');