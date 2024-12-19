CREATE TABLE configurations (
       id SERIAL PRIMARY KEY,
       name VARCHAR(50)
);

INSERT INTO configurations (name)
VALUES
    ('Mouse'),
    ('Teclado'),
    ('Sensibilidade'),
    ('Fone'),
    ('Resolucao'),
    ('Monitor');
