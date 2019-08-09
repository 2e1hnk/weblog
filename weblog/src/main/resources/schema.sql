drop table if exists authorities;
truncate table user;

CREATE TABLE authorities (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  FOREIGN KEY (username) REFERENCES user(username)
);

CREATE UNIQUE INDEX ix_auth_email on authorities (username,authority);
