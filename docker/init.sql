CREATE TABLE IF NOT EXISTS students (
  id uuid NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  middle_name VARCHAR(255) NOT NULL,
  
  PRIMARY KEY (id)
);