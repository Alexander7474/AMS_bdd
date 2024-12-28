CREATE TABLE user_group (
  id_group SERIAL PRIMARY KEY,
  nom VARCHAR(20) NOT NULL
);

CREATE TABLE permission (
  id_group INT,
  perm VARCHAR(100),
  PRIMARY KEY (id_group, perm),
  FOREIGN KEY (id_group) REFERENCES user_group(id_group)
);

CREATE TABLE utilisateur (
  id_user SERIAL PRIMARY KEY,
  id_group INT,
  username VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  FOREIGN KEY (id_group) REFERENCES user_group(id_group)
);

CREATE TABLE fournisseur (
  siret VARCHAR(14) PRIMARY KEY, -- passage de CHAR(14) à VARCHAR(14)
  nom VARCHAR(100) NOT NULL,
  adresse VARCHAR(100) NOT NULL,
  numero_tel VARCHAR(20), -- Augmenté à 20 pour plus de flexibilité
  email VARCHAR(100)
);

CREATE TABLE contact (
  id_contact SERIAL PRIMARY KEY,
  nom VARCHAR(100) NOT NULL,
  prenom VARCHAR(100),
  numero_tel VARCHAR(20), -- Augmenté à 20 pour plus de flexibilité
  email VARCHAR(100)
);

CREATE TABLE contact_fournisseur (
  siret VARCHAR(14),
  id_contact INT,
  PRIMARY KEY (siret, id_contact),
  FOREIGN KEY (siret) REFERENCES fournisseur(siret),
  FOREIGN KEY (id_contact) REFERENCES contact(id_contact)
);

CREATE TABLE produit (
  id_produit INT PRIMARY KEY,
  nom VARCHAR(55) NOT NULL,
  description VARCHAR(500), -- Correction du nom de la colonne
  categorie VARCHAR(100)
);

CREATE TABLE produit_fournisseur (
  id_produit INT,
  siret VARCHAR(14),
  prix_vente_uni DOUBLE PRECISION NOT NULL,
  PRIMARY KEY (siret, id_produit),
  FOREIGN KEY (siret) REFERENCES fournisseur(siret),
  FOREIGN KEY (id_produit) REFERENCES produit(id_produit)
);

CREATE TABLE contrat (
  id_contrat SERIAL PRIMARY KEY,
  siret VARCHAR(14),
  id_produit INT,
  prix_uni DOUBLE PRECISION NOT NULL,
  date_debut DATE NOT NULL,
  date_fin DATE NOT NULL,
  FOREIGN KEY (siret) REFERENCES fournisseur(siret),
  FOREIGN KEY (id_produit) REFERENCES produit(id_produit)
);

CREATE TABLE commande (
  id_commande BIGSERIAL PRIMARY KEY,
  id_produit INT,
  siret VARCHAR(14),
  quantite DOUBLE PRECISION NOT NULL, -- oublie de la quantité sur la précédente version
  FOREIGN KEY (siret) REFERENCES fournisseur(siret),
  FOREIGN KEY (id_produit) REFERENCES produit(id_produit)
);

CREATE TABLE achat (
  id_achat BIGSERIAL PRIMARY KEY,
  id_commande INT,
  prix_achat_uni DOUBLE PRECISION NOT NULL,
  date_achat DATE,
  FOREIGN KEY (id_commande) REFERENCES commande(id_commande)
);

CREATE TABLE lot_produit ( -- les lots de produits son dans le stock du magasin
  id_lot_produit BIGSERIAL PRIMARY KEY,
  id_produit INT,
  prix_vente_uni DOUBLE PRECISION NOT NULL,
  quantite DOUBLE PRECISION NOT NULL,
  peremption DATE,
  id_achat INT, -- De quelle achat provient le lot de produit
  FOREIGN KEY (id_produit) REFERENCES produit(id_produit),
  FOREIGN KEY (id_achat) REFERENCES achat(id_achat)
);

CREATE TABLE vente (
  id_vente BIGSERIAL PRIMARY KEY,
  id_lot_produit INT, -- Lot de produit d'ou vien le produit vendu
  date_vente DATE,
  prix_vente_uni DOUBLE PRECISION NOT NULL,
  quantite DOUBLE PRECISION NOT NULL,
  FOREIGN KEY (id_lot_produit) REFERENCES lot_produit(id_lot_produit)
);

INSERT INTO user_group(nom) VALUES ('admin');
INSERT INTO utilisateur(id_group, username, password) VALUES (1, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918');

INSERT INTO fournisseur VALUES ('11111111111111','the fournisseur','somewhere','0606060606','theemail@email.fr');
INSERT INTO produit(id_produit, nom,description,categorie) VALUES (1,'pomme','fucking apple','fruit');
INSERT INTO commande(id_produit,siret,quantite) VALUES (1,'11111111111111',5);
INSERT INTO achat(id_commande,prix_achat_uni,date_achat) VALUES (1,2.5,'20-12-2022');
INSERT INTO lot_produit(id_produit, prix_vente_uni, quantite, peremption, id_achat) VALUES (1,3.5,5,'20-12-2025',1);
INSERT INTO vente(id_lot_produit, date_vente, prix_vente_uni, quantite) VALUES(1,'25-12-2023',3.5,2);
 
 
 
 