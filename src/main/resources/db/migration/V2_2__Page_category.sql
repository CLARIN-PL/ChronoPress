ALTER TABLE koper.page ADD COLUMN category character varying;

UPDATE koper.page set category = 'education';

INSERT INTO koper.page(title, published, category) VALUES('home page', true, 'home');
INSERT INTO koper.property (key, value, lang) VALUES ('view.admin.panel.home.page.button','Strona domowa','PL');

