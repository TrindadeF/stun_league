CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    username VARCHAR(155),
    wins INTEGER,
    losses INTEGER,
    points DECIMAL (10, 3),
    user_id INTEGER,
    player_status INTEGER,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);