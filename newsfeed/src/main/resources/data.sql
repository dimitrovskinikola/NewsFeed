DROP TABLE IF EXISTS NewsFeed;

CREATE TABLE NewsFeed (
                              id INT AUTO_INCREMENT  PRIMARY KEY,
                              title VARCHAR(250),
                              description VARCHAR(5120),
                              publication_date TIMESTAMP,
                              image_url VARCHAR(250),
                              image BLOB
                               
);