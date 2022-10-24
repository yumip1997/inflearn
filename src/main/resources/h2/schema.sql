DROP TABLE IF EXISTS product;

CREATE TABLE product(
    product_id VARCHAR(100) PRIMARY KEY,
    product_name VARCHAR(100),
    sale_price INT,
    quantity INT
);