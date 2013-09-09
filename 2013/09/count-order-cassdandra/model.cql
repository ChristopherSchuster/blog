CREATE KEYSPACE  store
    WITH replication = {'class':'SimpleStrategy', 'replication_factor':2};


use store;

CREATE TABLE product  (
  id uuid PRIMARY KEY,
  category text,
  bar_code text,
  name text,
  price double,
 );

INSERT INTO product(id, category, bar_code, name, price) 
    VALUES(bdf3e070-4f75-11e1-a5c5-00215a17aed0, 'mobile', '4F5F4', 'TABLET ORANGE', 300);

INSERT INTO product(id, category, bar_code, name, price) 
    VALUES(d86cb040-0e02-11e0-9677-361934cac9ba, 'mobile', '4F5F4', 'PHONE BANANA', 100);

INSERT INTO product(id, category, bar_code, name, price) 
    VALUES(4066b980-2f09-11e0-8551-361934cac9ba, 'tv', '4F5F4', 'SMART TV', 100);


CREATE TABLE purchase_order  (
  id uuid ,
  product_id uuid,
  product_name text,
  quantity int,
  PRIMARY KEY(id, product_id)
);



CREATE TABLE counter_store(
    object text PRIMARY KEY,
    count counter,
);


UPDATE counter_store set count = count + 1 where object='product';
UPDATE counter_store set count = count + 3 where object='product';
UPDATE counter_store set count= count + 1 where object='purchase_order';

CREATE INDEX product_category_idx ON product(category);

INSERT INTO purchase_order(id, product_id, product_name, quantity)
    VALUES(5e15135e-4b72-11e1-b84d-00215a17aed0, bdf3e070-4f75-11e1-a5c5-00215a17aed0, 'TABLET ORANGE', 3);

INSERT INTO purchase_order(id, product_id, product_name, quantity)
    VALUES(5e15135e-4b72-11e1-b84d-00215a17aed0, d86cb040-0e02-11e0-9677-361934cac9ba, 'PHONE BANANA', 8);

INSERT INTO purchase_order(id, product_id, product_name, quantity) 
    VALUES(5e15135e-4b72-11e1-b84d-00215a17aed0, 4066b980-2f09-11e0-8551-361934cac9ba, 'SMART TV', 1);


INSERT INTO purchase_order(id, product_id, product_name, quantity)
    VALUES(c0a874f0-392a-11e0-a0df-361934cac9ba, d86cb040-0e02-11e0-9677-361934cac9ba, 'PHONE BANANA', 2);

INSERT INTO purchase_order(id, product_id, product_name, quantity) 
    VALUES(c0a874f0-392a-11e0-a0df-361934cac9ba, 4066b980-2f09-11e0-8551-361934cac9ba, 'SMART TV', 3);


DROP TABLE purchase_order;

CREATE TABLE purchase_order  (
  id uuid ,
  product_id uuid,
  product_name text,
  quantity int,
  PRIMARY KEY(id, product_id, quantity)
);
