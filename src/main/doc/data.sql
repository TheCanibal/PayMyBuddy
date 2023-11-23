INSERT INTO buddy (email, first_name, last_name, password, sold, role) 
VALUES ('jeandupont@mail.fr', 'Jean', 'Dupont', '$2a$10$l0VkiS.g/BdCAt8lWwG0weAMob4Ly1s43bJowr5Qu2yJZARjBja4K', 50, 'USER'),
       ('michelmartin@mail.fr', 'Michel', 'Martin', '$2a$10$Ai303wJuFUOhAoaEEFW/QOiHF/UuwFWpKAaWo1QZzpfqUAp0qg3l2', 101, 'USER'),
       ('gilbertlarousse@mail.fr', 'Gilbert', 'Larousse', '$2a$10$wcvef4JD7a61x/ntl71CRuutEAolBkGaqcp3NHd3DkYjTiYilzO4u', 100, 'USER'); 

INSERT INTO transaction (description, amount, fee, email_sender, email_reciever) 
VALUES ('Birthday', 100, 0.5, 'jeandupont@mail.fr', 'michelmartin@mail.fr'),
('Birthday', 100, 0.5, 'jeandupont@mail.fr', 'michelmartin@mail.fr'),
('Birthday', 100, 0.5, 'jeandupont@mail.fr', 'michelmartin@mail.fr'); 

INSERT INTO friend(email, email_friend) 
VALUES ('jeandupont@mail.fr', 'michelmartin@mail.fr');