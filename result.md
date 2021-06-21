1.Создание таблиц с постами главного экрана

CREATE TABLE authors(
   id         TEXT NOT NULL PRIMARY KEY,
   first_name TEXT NOT NULL,
   last_name  TEXT NOT NULL,
   nickname   TEXT,
   avatar_url TEXT,
   birth_day  TEXT NOT NULL
);

CREATE TABLE feed_posts(
   id         TEXT NOT NULL PRIMARY KEY,
   text       TEXT,
   image_url  TEXT,
   lon        REAL,
   lat        REAL,
   author_id  TEXT NOT NULL,
   likes      INT NOT NULL,
   liked      NUMERIC NOT NULL,
   FOREIGN KEY (author_id) REFERENCES authors(id)
);

2.Вставка в эту таблицу 3 записей

INSERT OR REPLACE INTO authors (id, first_name, last_name, nickname, avatar_url, birth_day)
VALUES
	("sdfgb456", "Andrey", "Rossinevich", "and_ross", "url=url=url", "1990-03-10"),
	("dfyh457rgj", "Nicolay", "Petrov", null, null, "2000-01-15"),
	("uiokjhkui", "Roman", "Nikolaev", "roma_nick", null, "1905-12-12");

INSERT OR REPLACE INTO feed_posts (id, text, image_url, lon, lat, author_id, likes, liked)
VALUES
	("asgdfsdf", "some post text", "dkjfgndkjgnkdj=url", 15.0, 20.4, "sdfgb456", 222, false),
	("sdfgxcbsd", "some post text", null, null, null, "dfyh457rgj", "888", true),
	("ghjdaftgd", "some post text", "image=urlsakmgn", null, null, "uiokjhkui", 2, true);


3. Чтение только тех записей, у которых свойство liked равно true
SELECT * FROM feed_posts JOIN authors ON feed_posts.author_id = authors.id WHERE liked = true;

4. Первая миграция
ALTER TABLE feed_posts ADD COLUMN new_column TEXT DEFAULT "default";

5. Вторая миграция
BEGIN TRANSACTION;
CREATE TABLE new_feed_posts(
   id          TEXT NOT NULL PRIMARY KEY,
   text        TEXT,
   image_url   TEXT,
   lon         REAL,
   lat         REAL,
   author_id   TEXT NOT NULL,
   likes_count INT NOT NULL,
   liked       NUMERIC NOT NULL,
   new_column  TEXT DEFAULT "default",
   FOREIGN KEY (author_id) REFERENCES authors(id)
);
INSERT OR ROLLBACK INTO new_feed_posts SELECT * FROM feed_posts;
DROP TABLE feed_posts;
ALTER TABLE new_feed_posts RENAME TO feed_posts;
COMMIT;

6. Третья миграция
BEGIN TRANSACTION;
ALTER TABLE feed_posts ADD COLUMN is_trending NUMERIC DEFAULT false ;
UPDATE OR ROLLBACK feed_posts SET is_trending = true WHERE likes_count > 3
COMMIT;
