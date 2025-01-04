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
  id_produit BIGSERIAL PRIMARY KEY,
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
INSERT INTO fournisseur VALUES ('22222222222222','logistic','lyon','0606060606','uuuuuuuu@email.fr');
INSERT INTO fournisseur VALUES ('33333333333333','other one','somewhere else','0606060606','ooooooooo@email.fr');

INSERT INTO produit(nom,description,categorie) VALUES ('pomme','fucking apple','fruit');
INSERT INTO produit(nom,description,categorie) VALUES ('bannane','bannanananananana','fruit');
INSERT INTO produit(nom,description,categorie) VALUES ('soda','1L of soda','drink');
INSERT INTO produit(nom,description,categorie) VALUES ('steak','300g de steak','meat');

INSERT INTO produit_fournisseur(id_produit,siret,prix_vente_uni) VALUES (1,'11111111111111',0.0);
INSERT INTO produit_fournisseur(id_produit,siret,prix_vente_uni) VALUES (2,'33333333333333',0.0);
INSERT INTO produit_fournisseur(id_produit,siret,prix_vente_uni) VALUES (3,'11111111111111',0.0);
INSERT INTO produit_fournisseur(id_produit,siret,prix_vente_uni) VALUES (2,'22222222222222',0.0);
INSERT INTO produit_fournisseur(id_produit,siret,prix_vente_uni) VALUES (4,'22222222222222',0.0);

INSERT INTO commande(id_produit,siret,quantite) VALUES (1,'11111111111111',89.0);
INSERT INTO commande(id_produit,siret,quantite) VALUES (3,'11111111111111',200.0);
INSERT INTO commande(id_produit,siret,quantite) VALUES (2,'22222222222222',65.8);
INSERT INTO commande(id_produit,siret,quantite) VALUES (3,'22222222222222',154.3);
INSERT INTO commande(id_produit,siret,quantite) VALUES (4,'33333333333333',150.0);

INSERT INTO contrat(siret, id_produit, prix_uni, date_debut, date_fin) VALUES ('11111111111111', 1, 0.8, '22-10-2000', '22-10-2100');
INSERT INTO contrat(siret, id_produit, prix_uni, date_debut, date_fin) VALUES ('33333333333333', 4, 5.4, '22-10-1989', '22-10-2001');

INSERT INTO contact(nom, prenom, numero_tel, email) VALUES('jack','jean','0505050505','jack@jean.com');
INSERT INTO contact(nom, prenom, numero_tel, email) VALUES('jhon','doe','0505050505','jhon@doe.com');
INSERT INTO contact(nom, prenom, numero_tel, email) VALUES('alice','jean','0505050505','alice@jean.com');
INSERT INTO contact(nom, prenom, numero_tel, email) VALUES('bob','jean','0505050505','bob@jean.com');

INSERT INTO contact_fournisseur(siret, id_contact) VALUES('11111111111111',1);
INSERT INTO contact_fournisseur(siret, id_contact) VALUES('11111111111111',3);
INSERT INTO contact_fournisseur(siret, id_contact) VALUES('22222222222222',1);
 
 