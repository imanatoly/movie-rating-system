-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_userroles_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_userroles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Movies table
CREATE TABLE movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    director VARCHAR(255),
    release_date DATE,
    genres VARCHAR(255) -- Added for MovieLens data
);
CREATE INDEX idx_movies_title ON movies(title);

-- Ratings table
CREATE TABLE ratings (
    id BIGSERIAL PRIMARY KEY,
    score INT NOT NULL,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    CONSTRAINT fk_ratings_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_ratings_movie FOREIGN KEY (movie_id) REFERENCES movies(id),
    CONSTRAINT uk_user_movie_rating UNIQUE (user_id, movie_id)
);
CREATE INDEX idx_ratings_user_id ON ratings(user_id);
