--DROP TABLE IF EXISTS users, transactions, add_friend, user_transaction;

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
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  description VARCHAR(250) NOT NULL,
  amount FLOAT NOT NULL
);

CREATE TABLE add_friend (
  email VARCHAR(100) REFERENCES users(email),
  email_friend VARCHAR(100) REFERENCES users(email),
  PRIMARY KEY (email, email_friend)
);

CREATE TABLE users_transactions (
  id_transaction INTEGER REFERENCES transactions(id_transaction),
  email VARCHAR(100) REFERENCES users(email),
  PRIMARY KEY (id_transaction, email)
  );