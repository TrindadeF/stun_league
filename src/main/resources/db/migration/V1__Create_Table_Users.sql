CREATE TABLE users (
    id SERIAL PRIMARY KEY ,
    name VARCHAR(155),
    email VARCHAR(155),
    cpf VARCHAR(155),
    birth_date DATE,
    password VARCHAR(155),
    image_profile_path  TEXT
);


