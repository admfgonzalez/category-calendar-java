CREATE TABLE IF NOT EXISTS TBL_CATEGORIES (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(70) NOT NULL,
    active BOOLEAN NOT NULL 
);

CREATE TABLE IF NOT EXISTS TBL_CATEGORY_SCHEDULES (
  id INT AUTO_INCREMENT PRIMARY KEY,
  schedule_date INT NOT NULL,
  fk_categories_id INT NOT NULL,
  active BOOLEAN NOT NULL
);

ALTER TABLE TBL_CATEGORY_SCHEDULES ADD FOREIGN KEY ( fk_categories_id ) REFERENCES TBL_CATEGORIES( id );