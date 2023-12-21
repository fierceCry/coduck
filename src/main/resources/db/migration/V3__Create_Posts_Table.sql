CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    post_category_id INT NOT NULL REFERENCES posts_categorys(id),
    title VARCHAR(255) NOT NULL,
    content VARCHAR(255),
    user_id INT NOT NULL REFERENCES users(id),
    image VARCHAR[],
    tags VARCHAR[],
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);