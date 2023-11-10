INSERT INTO users (email, first_name, last_name, password, sold, role) 
VALUES ('jeandupont@mail.fr', 'Jean', 'Dupont', '$2a$10$l0VkiS.g/BdCAt8lWwG0weAMob4Ly1s43bJowr5Qu2yJZARjBja4K', 50, 'USER'),
       ('michelmartin@mail.fr', 'Michel', 'Martin', '$2a$10$Ai303wJuFUOhAoaEEFW/QOiHF/UuwFWpKAaWo1QZzpfqUAp0qg3l2', 1, 'USER'),
       ('gilbertlarousse@mail.fr', 'Gilbert', 'Larousse', '$2a$10$wcvef4JD7a61x/ntl71CRuutEAolBkGaqcp3NHd3DkYjTiYilzO4u', 100, 'USER'); 

INSERT INTO transactions (id_transaction, first_name, last_name, description, amount) 
VALUES (1, 'Michel', 'Martin', 'Birthday', -100.5),
       (2, 'Jean', 'Dupont', 'Birthday', 100);     

INSERT INTO add_friend(email, email_friend) 
VALUES ('jeandupont@mail.fr', 'michelmartin@mail.fr');

INSERT INTO users_transactions (id_transaction, email)
VALUES (1, 'jeandupont@mail.fr'),
       (2, 'michelmartin@mail.fr');