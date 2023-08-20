INSERT INTO authority (authority_name)
VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO users (username, company_name, email, first_name, has_credit, last_name, password, phone_number)
VALUES ('tester', 'testcompanyname', 'test@test.nl', 'testname', 'false', 'testlastname', '$2a$10$DkY.h9MDU5Wk4.DPbWCEzOPKSUrIdmE3dmAefKzWeUS9hQcct6.Ba', '01012345678'),
       ('tester2', 'testcompanyname2', 'test2@test.nl', 'testname2', 'true', 'testlastname2', '$2a$10$YSjOxHQ2IeRiBN7TInSyjeRWT9gJapwdGhQyBt3eDmNlGrw0g7Etm', '01098765432');

INSERT INTO users_authorities (username, authority_name)
VALUES  ('tester', 'ROLE_USER'),
        ('tester2', 'ROLE_USER'),
        ('tester', 'ROLE_ADMIN');

INSERT INTO address (address_line_1, address_line_2, city, zipcode, username)
VALUES ('Testerstreet 1', null, 'Testerton', '1234 AB', 'tester'),
       ('Test-square 10', null, 'Testerton', '9876 BA', 'tester'),
       ('Testerlane 2', 'Apartment A', 'Testcity', '4321 ZX', 'tester2');

INSERT INTO product (id, availability, description, name, price, tax)
VALUES ('1', 'true', 'Description of test product', 'Test product', '15.25', '21.0'),
       ('2', 'true', 'Description of test2 product', 'Test2 product', '16.75', '9.0');

INSERT INTO web_order (id, order_date_time, web_order_status, address_id, username)
VALUES ('1', '2023-08-19T14:18:22.330552', 'PENDING', '1', 'tester');

INSERT INTO web_order_detail (id, quantity, product_id, web_order_id)
VALUES ('1', '2', '1', '1');