CREATE TABLE communication_comments (
    id SERIAL PRIMARY KEY,
    post_id INT NOT NULL REFERENCES communication(id),
    user_id INT NOT NULL REFERENCES users(id),
    content VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
