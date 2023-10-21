CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY ,
    description VARCHAR(256) NOT NULL ,
    price INT NOT NULL
);

INSERT INTO products (description, price)
VALUES
('Book', 25),
('Plate', 100),
('Chips', 5),
('Cola', 6);