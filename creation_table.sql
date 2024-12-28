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

CREATE TABLE vente (
  id_vente BIGSERIAL PRIMARY KEY,
  id_produit INT, -- Correction du nom de la colonne
  date_vente DATE,
  prix_vente_uni DOUBLE PRECISION NOT NULL,
  quantite DOUBLE PRECISION NOT NULL,
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

CREATE TABLE lot_produit (
  id_lot_produit BIGSERIAL PRIMARY KEY,
  id_produit INT,
  prix_vente_uni DOUBLE PRECISION NOT NULL,
  quantite DOUBLE PRECISION NOT NULL,
  peremption DATE,
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


