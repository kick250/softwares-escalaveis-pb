CREATE TABLE stock_items (
    id SERIAL PRIMARY KEY,
    price NUMERIC(12, 2),
    quantity INTEGER NOT NULL,
    product_id INT REFERENCES products(id),
    stock_id INT REFERENCES stocks(id)
);