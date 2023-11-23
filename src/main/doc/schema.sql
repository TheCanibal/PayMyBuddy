-- DROP TABLE IF EXISTS buddy, transaction, friend;

CREATE TABLE buddy (
  id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(100) UNIQUE NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  password VARCHAR(250) NOT NULL,
  sold FLOAT NOT NULL,
  role VARCHAR(25) NOT NULL
);

CREATE TABLE transaction (
  id_transaction INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  description VARCHAR(250) NOT NULL,
  amount FLOAT NOT NULL,
  fee FLOAT NOT NULL,
  email_sender VARCHAR(100) NOT NULL REFERENCES buddy(email),
  email_reciever VARCHAR(100) NOT NULL REFERENCES buddy(email)
);

CREATE TABLE friend (
  email VARCHAR(100) NOT NULL REFERENCES buddy(email),
  email_friend VARCHAR(100) NOT NULL REFERENCES buddy(email),
  PRIMARY KEY (email, email_friend)
);