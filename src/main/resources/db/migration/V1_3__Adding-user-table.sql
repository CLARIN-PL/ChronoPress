-- Table holding application properties

CREATE TABLE koper.user
(
  id bigserial NOT NULL,
  username character varying,
  password character varying,
  active boolean,

  CONSTRAINT user_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE koper.user
  OWNER TO chrono;

CREATE SEQUENCE koper.user_ref_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE koper.user_ref_id_seq
  OWNER TO chrono;