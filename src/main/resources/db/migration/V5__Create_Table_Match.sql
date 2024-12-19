CREATE TABLE match_players (
       id SERIAL PRIMARY KEY,
       match_date DATE,
       id_player INTEGER,
       wins INTEGER,
       losses INTEGER,
       FOREIGN KEY (id_player) REFERENCES players(id)
);