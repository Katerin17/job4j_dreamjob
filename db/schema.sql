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