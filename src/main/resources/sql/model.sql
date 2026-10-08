-- ----------------------------------------------------------
-- Script MYSQL pour mcd 
-- ----------------------------------------------------------


-- ----------------------------
-- Table: registration_form
-- ----------------------------
CREATE TABLE registration_form (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT registration_form_PK PRIMARY KEY (id),
  CONSTRAINT email_UNQ UNIQUE (email)
)ENGINE=InnoDB;


-- ----------------------------
-- Table: games
-- ----------------------------
CREATE TABLE games (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(100) NOT NULL,
  cover VARCHAR(255) NOT NULL,
  CONSTRAINT games_PK PRIMARY KEY (id)
)ENGINE=InnoDB;


-- ----------------------------
-- Table: authentication
-- ----------------------------
CREATE TABLE authentication (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  id_registration_form INT NOT NULL,
  CONSTRAINT authentication_PK PRIMARY KEY (id),
  CONSTRAINT username_UNQ UNIQUE (username),
  CONSTRAINT authentication_id_registration_form_FK FOREIGN KEY (id_registration_form) REFERENCES registration_form (id)
)ENGINE=InnoDB;


-- ----------------------------
-- Table: user_games
-- ----------------------------
CREATE TABLE user_games (
  id INT NOT NULL AUTO_INCREMENT,
  played TINYINT(1) DEFAULT FALSE,
  platinum TINYINT(1) DEFAULT FALSE,
  100_percent TINYINT(1) DEFAULT FALSE,
  wishlist_games TINYINT(1) DEFAULT FALSE,
  wishlist_platinum TINYINT(1) DEFAULT FALSE,
  game_rating SMALLINT,
  platinum_difficulty SMALLINT,
  time_played_hours DECIMAL(6,2),
  id_authentication INT NOT NULL,
  id_games INT NOT NULL,
  CONSTRAINT user_games_PK PRIMARY KEY (id),
  CONSTRAINT user_games_id_authentication_FK FOREIGN KEY (id_authentication) REFERENCES authentication (id),
  CONSTRAINT user_games_id_games_FK FOREIGN KEY (id_games) REFERENCES games (id)
)ENGINE=InnoDB;

