CREATE DATABASE faces_db;

CREATE SEQUENCE ids;
CREATE SEQUENCE links_ids;


CREATE TABLE profiles (
  id            INTEGER PRIMARY KEY DEFAULT NEXTVAL('ids'),
  username      TEXT UNIQUE NOT NULL,
  password      TEXT        NOT NULL,
  url_name      TEXT UNIQUE,
  creation_date TIMESTAMP,
  first_name    TEXT,
  last_name     TEXT,
  birthday      TEXT,
  hometown      TEXT,
  email         TEXT,
  phone         TEXT,
  work_phone    TEXT,
  photo         TEXT,
  language      TEXT
);

CREATE TABLE links (
  id       INTEGER PRIMARY KEY DEFAULT NEXTVAL('links_ids'),
  user_id  INTEGER,
  group_id INTEGER,
  link     TEXT,
  FOREIGN KEY (user_id) REFERENCES profiles (id)
);

