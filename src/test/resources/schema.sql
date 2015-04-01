DROP TABLE attacks IF EXISTS;
DROP TABLE spellCasts IF EXISTS;
DROP TABLE provinces IF EXISTS;
DROP TABLE people IF EXISTS;
DROP TABLE schema_information IF EXISTS;

CREATE TABLE schema_information (
	lock CHAR(1) NOT NULL DEFAULT 'X',
	version_major INT NOT NULL,
	version_minor INT NOT NULL,
	version_patch INT NOT NULL,
	CONSTRAINT PK_Lock PRIMARY KEY (Lock),
	CONSTRAINT CK_Locked CHECK (lock='X')
);

CREATE TABLE people (
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE provinces (
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	personId BIGINT, 
	name VARCHAR(50) NOT NULL,
	race TINYINT NOT NULL,
	personality TINYINT NOT NULL,
	kingdom INT,
	island INT,
	FOREIGN KEY (personId) REFERENCES people(id) ON DELETE SET NULL
);

CREATE TABLE spellCasts (
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	spellId VARCHAR(10) NOT NULL,
	lastHour INT NOT NULL
);

CREATE TABLE attacks (
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	personId BIGINT,
	person VARCHAR(20),
	returnDate TIMESTAMP NOT NULL,
	FOREIGN KEY (personId) REFERENCES people(id) ON DELETE SET NULL
);