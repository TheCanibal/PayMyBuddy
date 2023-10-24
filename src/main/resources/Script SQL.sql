DROP TABLE IF EXISTS users;

CREATE TABLE users (
  email VARCHAR(100) PRIMARY KEY NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  password VARCHAR(250) NOT NULL,
  sold INT NOT NULL,
  role VARCHAR(25) NOT NULL
);

INSERT INTO users (email, first_name, last_name, password, sold, role) 
VALUES ('jeandupont@mail.fr', 'Jean', 'Dupont', '$2a$10$l0VkiS.g/BdCAt8lWwG0weAMob4Ly1s43bJowr5Qu2yJZARjBja4K', 50, 'USER'),
       ('michelmartin@mail.fr', 'Michel', 'Martin', '$2a$10$Ai303wJuFUOhAoaEEFW/QOiHF/UuwFWpKAaWo1QZzpfqUAp0qg3l2', 1, 'USER'),
       ('gilbertlarousse@mail.fr', 'Gilbert', 'Larousse', '$2a$10$wcvef4JD7a61x/ntl71CRuutEAolBkGaqcp3NHd3DkYjTiYilzO4u', 100, 'USER'); 


DROP TABLE IF EXISTS transactions;

CREATE TABLE transactions (
  id_transaction INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  description VARCHAR(250) NOT NULL,
  amount INT NOT NULL
);

DROP TABLE IF EXISTS add_friend;

CREATE TABLE add_friend (
  email VARCHAR(100) REFERENCES users(email),
  email_friend VARCHAR(100) REFERENCES users(email),
  PRIMARY KEY (email, email_friend)
);

DROP TABLE IF EXISTS user_transaction;

CREATE TABLE user_transaction (
  id_transaction INTEGER REFERENCES transactions(id_transaction),
  email VARCHAR(100) REFERENCES users(email),
  PRIMARY KEY (id_transaction, email)
  );