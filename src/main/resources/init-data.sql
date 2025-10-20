-- Création de la base de données si elle n'existe pas
CREATE DATABASE IF NOT EXISTS ecommerce;
USE ecommerce;

-- Suppression des tables existantes pour éviter les conflits
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS ligne_commande;
DROP TABLE IF EXISTS commandes;
DROP TABLE IF EXISTS ligne_panier;
DROP TABLE IF EXISTS paniers;
DROP TABLE IF EXISTS produits;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS utilisateurs;
SET FOREIGN_KEY_CHECKS = 1;

-- Création des tables (JPA les créera normalement, mais au cas où)
-- Table utilisateurs
CREATE TABLE utilisateurs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    adresse TEXT,
    telephone VARCHAR(20),
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table categories
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    description TEXT
);

-- Table produits
CREATE TABLE produits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    prix DOUBLE NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    image VARCHAR(255),
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    categorie_id BIGINT,
    FOREIGN KEY (categorie_id) REFERENCES categories(id)
);

-- Table paniers
CREATE TABLE paniers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    utilisateur_id BIGINT UNIQUE,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);

-- Table ligne_panier
CREATE TABLE ligne_panier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantite INT NOT NULL,
    prix_unitaire DOUBLE NOT NULL,
    panier_id BIGINT,
    produit_id BIGINT,
    FOREIGN KEY (panier_id) REFERENCES paniers(id),
    FOREIGN KEY (produit_id) REFERENCES produits(id)
);

-- Table commandes
CREATE TABLE commandes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_commande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(50) NOT NULL,
    montant_total DOUBLE NOT NULL,
    utilisateur_id BIGINT,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);

-- Table ligne_commande
CREATE TABLE ligne_commande (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantite INT NOT NULL,
    prix_unitaire DOUBLE NOT NULL,
    commande_id BIGINT,
    produit_id BIGINT,
    FOREIGN KEY (commande_id) REFERENCES commandes(id),
    FOREIGN KEY (produit_id) REFERENCES produits(id)
);

-- Insertion des données de test
-- Catégories
INSERT INTO categories (nom, description) VALUES 
('Électronique', 'Produits électroniques et gadgets'),
('Vêtements', 'Vêtements et accessoires de mode'),
('Livres', 'Livres, ebooks et publications'),
('Maison', 'Décoration et articles pour la maison'),
('Sports', 'Équipements et vêtements de sport');

-- Produits
INSERT INTO produits (nom, description, prix, stock, image, categorie_id) VALUES 
('Smartphone XYZ', 'Smartphone dernière génération avec écran 6.5 pouces', 699.99, 50, '/resources/images/smartphone.jpg', 1),
('Ordinateur portable Pro', 'Ordinateur portable puissant pour professionnels', 1299.99, 25, '/resources/images/laptop.jpg', 1),
('Écouteurs sans fil', 'Écouteurs bluetooth avec réduction de bruit', 149.99, 100, '/resources/images/earbuds.jpg', 1),
('T-shirt classique', 'T-shirt en coton 100% biologique', 19.99, 200, '/resources/images/tshirt.jpg', 2),
('Jean slim', 'Jean coupe slim confortable', 49.99, 150, '/resources/images/jeans.jpg', 2),
('Veste d\'hiver', 'Veste chaude et imperméable', 89.99, 75, '/resources/images/jacket.jpg', 2),
('Roman bestseller', 'Le dernier roman à succès de l\'année', 24.99, 120, '/resources/images/book1.jpg', 3),
('Guide de programmation', 'Apprenez à coder en Java', 34.99, 80, '/resources/images/book2.jpg', 3),
('Lampe de bureau LED', 'Lampe de bureau ajustable avec port USB', 39.99, 60, '/resources/images/lamp.jpg', 4),
('Ensemble de cuisine', 'Set de 5 ustensiles de cuisine en inox', 29.99, 40, '/resources/images/kitchenset.jpg', 4),
('Ballon de football', 'Ballon de football professionnel', 19.99, 90, '/resources/images/football.jpg', 5),
('Tapis de yoga', 'Tapis antidérapant pour yoga et fitness', 24.99, 70, '/resources/images/yogamat.jpg', 5);

-- Utilisateurs (mot de passe: 'password' pour tous)
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, adresse, telephone) VALUES 
('Dupont', 'Jean', 'jean.dupont@example.com', 'password', '123 Rue de Paris, 75001 Paris', '0123456789'),
('Martin', 'Sophie', 'sophie.martin@example.com', 'password', '456 Avenue des Champs-Élysées, 75008 Paris', '0234567890'),
('Dubois', 'Pierre', 'pierre.dubois@example.com', 'password', '789 Boulevard Saint-Michel, 75005 Paris', '0345678901');

-- Paniers
INSERT INTO paniers (utilisateur_id) VALUES 
(1),
(2),
(3);

-- Lignes de panier
INSERT INTO ligne_panier (panier_id, produit_id, quantite, prix_unitaire) VALUES 
(1, 1, 1, 699.99),
(1, 3, 2, 149.99),
(2, 4, 3, 19.99),
(2, 5, 1, 49.99),
(3, 8, 1, 34.99),
(3, 10, 2, 29.99);

-- Commandes
INSERT INTO commandes (utilisateur_id, statut, montant_total) VALUES 
(1, 'VALIDEE', 249.98),
(2, 'LIVREE', 109.97),
(3, 'EN_ATTENTE', 94.97);

-- Lignes de commande
INSERT INTO ligne_commande (commande_id, produit_id, quantite, prix_unitaire) VALUES 
(1, 3, 1, 149.99),
(1, 11, 5, 19.99),
(2, 4, 2, 19.99),
(2, 12, 1, 24.99),
(2, 10, 1, 29.99),
(3, 8, 1, 34.99),
(3, 9, 1, 39.99),
(3, 7, 1, 24.99);