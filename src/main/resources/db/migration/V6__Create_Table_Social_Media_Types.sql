CREATE TABLE social_media_type (
       id SERIAL PRIMARY KEY,
       name VARCHAR(50)
);

INSERT INTO social_media_type (name)
VALUES
    ('Discord'),
    ('Instagram'),
    ('Facebook'),
    ('TikTok'),
    ('Youtube'),
    ('Twitch'),
    ('Twitter');
