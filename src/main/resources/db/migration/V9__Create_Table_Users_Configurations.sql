CREATE TABLE users_configurations (
       id SERIAL PRIMARY KEY,
       user_id INTEGER,
       configuration_id INTEGER,
       value VARCHAR(255),
       FOREIGN KEY (user_id) REFERENCES users(id),
       FOREIGN KEY (configuration_id) REFERENCES configurations(id)
);