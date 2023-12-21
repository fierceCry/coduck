CREATE TABLE communication_comments_likes (
    id SERIAL PRIMARY KEY,
    communication_comments_id INT NOT NULL REFERENCES communication_comments(id),
    user_id INT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
