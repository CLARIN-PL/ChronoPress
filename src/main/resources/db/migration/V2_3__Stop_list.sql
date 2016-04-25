CREATE TABLE koper.stop_list
(
    id bigserial NOT NULL,
    name character varying,
    CONSTRAINT stop_list_pkey PRIMARY KEY (id)
)
  WITH (
    OIDS=FALSE
);

ALTER TABLE koper.stop_list
   OWNER TO chrono;

CREATE SEQUENCE koper.stop_list_ref_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

ALTER TABLE koper.stop_list_ref_id_seq
     OWNER TO chrono;

INSERT INTO koper.property (key, value, lang) VALUES ('view.admin.stop.list.title','Stop lista','PL');
INSERT INTO koper.property (key, value, lang) VALUES ('label.add.stop.list.item.name','Dodaj element','PL');
INSERT INTO koper.property (key, value, lang) VALUES ('label.remove.stop.list.item.name','Usuń element','PL');
