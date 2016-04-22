-- Table holding application properties

CREATE TABLE koper.proper_names
(
  id bigserial NOT NULL,
  original_proper_name character varying,
  alias character varying,
  type character varying,
  lat float4,
  lon float4,
  processed boolean,
  CONSTRAINT proper_names_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE koper.proper_names
  OWNER TO chrono;

CREATE SEQUENCE koper.proper_names_ref_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

ALTER TABLE koper.proper_names_ref_id_seq
  OWNER TO chrono;

CREATE TABLE koper.sentence_proper_name
(
  id bigserial NOT NULL,
  sentence_id int4,
  proper_name_id int8,
  CONSTRAINT sentence_proper_name_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE koper.sentence_proper_name
  OWNER TO chrono;

CREATE SEQUENCE koper.sentence_proper_name_ref_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;