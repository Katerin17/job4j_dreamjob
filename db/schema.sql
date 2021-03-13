CREATE TABLE posts (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created date
);
CREATE TABLE candidates (
    id SERIAL PRIMARY KEY,
    name TEXT
);
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);