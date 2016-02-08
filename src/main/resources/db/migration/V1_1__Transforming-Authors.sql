ALTER TABLE koper.text ADD COLUMN authors_parsed text;

UPDATE koper.text SET authors_parsed = authors;