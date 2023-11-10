#DROP TABLE IF EXISTS users, transactions, add_friend;

CREATE TABLE users (
  email VARCHAR(100) PRIMARY KEY NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  password VARCHAR(250) NOT NULL,
  sold FLOAT NOT NULL,
  role VARCHAR(25) NOT NULL
);

CREATE TABLE transactions (
  id_transaction INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  description VARCHAR(250) NOT NULL,
  amount FLOAT NOT NULL,
  email VARCHAR(100) NOT NULL REFERENCES users(email),
  email_friend VARCHAR(100) NOT NULL REFERENCES users(email)
);

CREATE TABLE add_friend (
  email VARCHAR(100) REFERENCES users(email),
  email_friend VARCHAR(100) REFERENCES users(email),
  PRIMARY KEY (email, email_friend)
);