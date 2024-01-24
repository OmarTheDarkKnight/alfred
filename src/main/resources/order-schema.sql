DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
    id int NOT NULL,
    product_name varchar(100) NOT NULL ,
    order_amount int NOT NULL ,
    user_id int NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO orders (id, product_name, order_amount, user_id)
VALUES (1, 'Line launcher', 10, 1), (2, 'Smoke bomb', 100, 1);