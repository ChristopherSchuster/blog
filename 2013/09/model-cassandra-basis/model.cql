CREATE KEYSPACE  store
    WITH replication = {'class':'SimpleStrategy', 'replication_factor':2};


use store;

CREATE TABLE product  (
  id uuid PRIMARY KEY,
  bar_code text,
  name text,
  price double
 );


CREATE TABLE purchase_order  (
  id uuid PRIMARY KEY,
  product_id uuid,
  product_name text,
  quantity int
);



INSERT INTO product(id, bar_code, name, price) VALUES(bdf3e070-4f75-11e1-a5c5-00215a17aed0, '4F5F4', 'TABLET', 300);

INSERT INTO product(id, bar_code, name, price) VALUES(0ba10600-6712-11e1-b653-00215a17aed0, '4Fu84', 'TV', 800);

INSERT INTO product(id, bar_code, name, price) VALUES(14bc9100-6712-11e1-990c-00215a17aed0, '4R5F', 'PC', 400);

INSERT INTO product(id, bar_code, name, price) VALUES(1a16bc50-6714-11e1-8366-00215a17aed0, '8D4F', 'WATCH', 250);


INSERT INTO purchase_order(id, product_id, product_name, quantity)
    VALUES(35643ec0-1690-11e2-947c-00215a17aed0, bdf3e070-4f75-11e1-a5c5-00215a17aed0, 'TABLET', 3);

INSERT INTO purchase_order(id, product_id, product_name, quantity) 
    VALUES(35643ec0-1690-11e2-947c-00215a17aed0, 0ba10600-6712-11e1-b653-00215a17aed0, 'TV', 8);

INSERT INTO purchase_order(id, product_id, product_name, quantity)
    VALUES(35643ec0-1690-11e2-947c-00215a17aed0, 14bc9100-6712-11e1-990c-00215a17aed0, 'PC', 4);

INSERT INTO purchase_order(id, product_id, product_name, quantity)
    VALUES(35643ec0-1690-11e2-947c-00215a17aed0, 1a16bc50-6714-11e1-8366-00215a17aed0, 'WATCH', 2);

select * from purchase_order;
select * from purchase_order where product_id=08d181c0-dc83-11e1-9c22-00215a17aed0;

DROP TABLE purchase_order;
CREATE TABLE purchase_order  (
  id uuid,
  product_id uuid,
  product_name text,
  quantity int,
  PRIMARY KEY(id, product_id)
);

INSERT INTO purchase_order(id, product_id, product_name, quantity)
    VALUES(35643ec0-1690-11e2-947c-00215a17aed0, bdf3e070-4f75-11e1-a5c5-00215a17aed0, 'TABLET', 3);

INSERT INTO purchase_order(id, product_id, product_name, quantity) 
    VALUES(35643ec0-1690-11e2-947c-00215a17aed0, 0ba10600-6712-11e1-b653-00215a17aed0, 'TV', 8);

INSERT INTO purchase_order(id, product_id, product_name, quantity)
    VALUES(35643ec0-1690-11e2-947c-00215a17aed0, 14bc9100-6712-11e1-990c-00215a17aed0, 'PC', 4);

INSERT INTO purchase_order(id, product_id, product_name, quantity)
    VALUES(35643ec0-1690-11e2-947c-00215a17aed0, 1a16bc50-6714-11e1-8366-00215a17aed0, 'WATCH', 2);

select * from purchase_order;   

INSERT INTO purchase_order(id, product_id, product_name, quantity)
    VALUES(5e15135e-4b72-11e1-b84d-00215a17aed0, 1a16bc50-6714-11e1-8366-00215a17aed0, 'WATCH', 8);

INSERT INTO purchase_order(id, product_id, product_name, quantity) 
    VALUES(5e15135e-4b72-11e1-b84d-00215a17aed0, 0ba10600-6712-11e1-b653-00215a17aed0, 'TV', 1);


SELECT * from purchase_order where id=5e15135e-4b72-11e1-b84d-00215a17aed0;


CREATE TABLE product_purchase_order  (
  id uuid PRIMARY KEY,
  purchase_id uuid,
  product_id uuid,
  product_name text,
  quantity int
);

CREATE INDEX ON product_purchase_order(purchase_id);
CREATE INDEX ON product_purchase_order(product_id);

INSERT INTO product_purchase_order(id, purchase_id, product_id, product_name, quantity)
    VALUES(f5b87f80-913c-11e1-8995-00215a17aed0 ,35643ec0-1690-11e2-947c-00215a17aed0, 
           bdf3e070-4f75-11e1-a5c5-00215a17aed0, 'TABLET', 3);

INSERT INTO product_purchase_order(id, purchase_id, product_id, product_name, quantity)
    VALUES(54e793f0-a11a-11e1-bba9-00215a17aed0, 35643ec0-1690-11e2-947c-00215a17aed0,
           0ba10600-6712-11e1-b653-00215a17aed0, 'TV', 8);

INSERT INTO product_purchase_order(id, purchase_id, product_id, product_name, quantity)
    VALUES(9ba4eda2-38a6-11e2-b72b-00215a17aed0, 35643ec0-1690-11e2-947c-00215a17aed0, 
           14bc9100-6712-11e1-990c-00215a17aed0, 'PC', 4);

INSERT INTO product_purchase_order(id, purchase_id, product_id, product_name, quantity)
    VALUES(4b01bf90-7853-11e1-85e9-00215a17aed0, 35643ec0-1690-11e2-947c-00215a17aed0, 
           1a16bc50-6714-11e1-8366-00215a17aed0, 'WATCH', 2);

INSERT INTO product_purchase_order(id, purchase_id, product_id, product_name, quantity)
    VALUES(d33dc000-1d1e-11e2-943c-00215a17aed0, 5e15135e-4b72-11e1-b84d-00215a17aed0,
           1a16bc50-6714-11e1-8366-00215a17aed0, 'WATCH', 8);

INSERT INTO product_purchase_order(id, purchase_id, product_id, product_name, quantity) 
    VALUES(862fdbd0-26ac-11e2-95ee-00215a17aed0, 5e15135e-4b72-11e1-b84d-00215a17aed0, 0ba10600-6712-11e1-b653-00215a17aed0, 'TV', 1);   


SELECT *  FROM product_purchase_order  WHERE purchase_id=5e15135e-4b72-11e1-b84d-00215a17aed0;

SELECT *  FROM product_purchase_order WHERE product_id=0ba10600-6712-11e1-b653-00215a17aed0;
