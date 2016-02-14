-- Table holding application properties

CREATE TABLE koper.property
(
  id bigserial NOT NULL,
  key character varying,
  value character varying,
  lang character varying,

  CONSTRAINT property_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE koper.property
  OWNER TO chrono;

CREATE SEQUENCE koper.property_ref_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE koper.property_ref_id_seq
  OWNER TO chrono;