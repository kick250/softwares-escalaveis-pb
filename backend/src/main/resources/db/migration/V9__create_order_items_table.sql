CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    deleted BOOLEAN NOT NULL DEFAULT false,
    quantity INTEGER NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    order_id INTEGER NOT NULL REFERENCES orders(id),
    stock_item_id INTEGER NOT NULL REFERENCES stock_items(id)
);