CREATE TABLE users_social_media (
       id SERIAL PRIMARY KEY,
       user_id INTEGER,
       media_id INTEGER,
       value VARCHAR(255),
       FOREIGN KEY (user_id) REFERENCES users(id),
       FOREIGN KEY (media_id) REFERENCES social_media_type(id)
);