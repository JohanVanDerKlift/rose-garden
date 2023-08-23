INSERT INTO authority (authority_name)
VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO users (username, company_name, email, first_name, has_credit, last_name, password, phone_number)
VALUES ('admin', 'admin', 'admin@admin.com', 'admin', 'false', 'admin', '$2y$10$dDTdrb7fdYZE/TX8L4i5nOWSK.eYgvIqpJG2gPkisADkJ8A6HkW5a', 'admin'),
       ('tester', 'testcompanyname', 'test@test.nl', 'testname', 'false', 'testlastname', '$2a$10$DkY.h9MDU5Wk4.DPbWCEzOPKSUrIdmE3dmAefKzWeUS9hQcct6.Ba', '0101234567');

INSERT INTO users_authorities (username, authority_name)
VALUES  ('admin', 'ROLE_USER'),
        ('admin', 'ROLE_ADMIN');

INSERT INTO address (address_line_1, address_line_2, city, zipcode, username)
VALUES ('Testerstreet 1', null, 'Testerton', '1234 AB', 'admin'),
       ('Test-square 10', null, 'Testerton', '9876 BA', 'admin'),
       ('Testerlane 2', 'Apartment A', 'Testcity', '4321 ZX', 'tester');

INSERT INTO product (id, tax, availability, name, description,  price)
VALUES ('1', '9', 'true', 'Kleine loempia', '', '4.50'),
       ('2', '9', 'true', 'Loempia', '', '3.50'),
       ('3', '9', 'true', 'Emping', '', '2.80'),
       ('4', '9', 'true', 'Kroepoek oedang', '', '3.00'),
       ('5', '9', 'true', 'Pangsit goreng', '', '4.20'),
       ('11', '9', 'true', 'Kippensoep', '', '2.80'),
       ('12', '9', 'true', 'Tomatensoep', '', '3.00'),
       ('13', '9', 'true', 'Champignonsoep', '', '4.00'),
       ('14', '9', 'true', 'Haaienvinnensoep', '', '3.00'),
       ('15', '9', 'true', 'Wan tan soep', '', '5.00'),
       ('16', '9', 'true', 'Haaienvinnensoep (met krab)', '', '5.00'),
       ('27', '9', 'true', 'Bami goreng', '', '5.30'),
       ('35', '9', 'true', 'Tjap woei mei', '', '13.00'),
       ('40', '9', 'true', 'Mihoen goreng singapore', '', '12.00'),
       ('41', '9', 'true', 'Harmon mihoen', '', '12.00'),
       ('42', '9', 'true', 'Originele chinese bami', '', '10.50'),
       ('52', '9', 'true', 'Taugee met fijn gesneden kip', '', '11.00'),
       ('54', '9', 'true', 'Tjap tjoy', '', '11.00'),
       ('58', '9', 'true', 'Foe young Hai met gesneden champignons', '', '8.50'),
       ('60', '9', 'true', 'Foe young hai met gesneden kip', '', '11.00'),
       ('64', '9', 'true', 'Koe lau yoek', '', '11.00'),
       ('66', '9', 'true', 'Babi pangang', '', '12.00'),
       ('68', '9', 'true', 'Babi ketjap', '', '12.30'),
       ('98', '9', 'true', 'Gesneden ossenhaas met tausie saus', '', '14.00'),
       ('100', '9', 'true', 'Gesneden ossenhaas met oestersaus', '', '16.50'),
       ('103', '9', 'true', 'Koe lau ha', '', '19.00'),
       ('104', '9', 'true', 'Gepaneerde grote chinese garnalen', '', '19.00'),
       ('118', '9', 'true', 'Tongfilet met gember en oestersaus', '', '21.00'),
       ('120', '9', 'true', 'Gebakken kikkerbillen', '', '15.50'),
       ('124', '9', 'true', 'Nasi rames', '', '9.20'),
       ('129', '9', 'true', 'Daging smoor', '', '11.50'),
       ('130', '9', 'true', 'Gado gado', '', '3.70'),
       ('135', '9', 'true', 'Witte rijst', '', '3.00'),
       ('138', '9', 'true', 'Koen bo kai', 'Gekruide kip met szechuan pepers', '15.50'),
       ('148', '9', 'true', 'Tiel jiem ha', 'Gebakken grote garnalen met paprika en knoflook', '21.50'),
       ('151', '9', 'true', 'Hak tiel ngau', 'Ossenhaas met zwarte peper', '20.00'),
       ('154', '9', 'true', 'Satja ngau', 'Ossenhaas met fijn gekruide saus', '20.00'),
       ('163', '9', 'true', 'Tsi po yu', 'Tongfilet met verse gember', '22.00'),
       ('179', '9', 'true', 'Hong sieuw taufoe', 'Gebakken taufoe met oestersaus', '10.00'),
       ('180', '9', 'true', 'Lo hon tsaai', 'Chinese vegetarische mix', '10.00'),
       ('201', '21', 'true', 'Coca cola', 'Blik 330ml', '2.50'),
       ('202', '21', 'true', 'Sisi orange', 'Blik 330ml', '2.50'),
       ('203', '21', 'true', 'Heineken', 'Blik 330ml (Alcohol houdend product 18+)', '3.50');