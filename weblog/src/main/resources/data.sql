INSERT INTO user (id, username, password, enabled) VALUES (1, 'mattcarus@gmail.com', '$2a$10$O6JPyxgfMuPwIf28MEjlrebqUc04H0wxi6OcfTIZCH/6/6AXzEB7G', 1);

INSERT INTO authorities (username, authority) values ('mattcarus@gmail.com', 'ROLE_USER');