CREATE TABLE koper.audience
(
    id bigserial NOT NULL,
    name character varying,
    CONSTRAINT audience_pkey PRIMARY KEY (id)
)
  WITH (
    OIDS=FALSE
);

CREATE TABLE koper.audience_tiles
(
    audience_id bigserial NOT NULL,
    journalTitle character varying
)
  WITH (
    OIDS=FALSE
);

ALTER TABLE koper.audience
   OWNER TO chrono;

CREATE SEQUENCE koper.audience_ref_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

ALTER TABLE koper.audience_ref_id_seq
     OWNER TO chrono;

-----------------------------------------------
CREATE TABLE koper.lexical_field
(
    id bigserial NOT NULL,
    group_name character varying,
    CONSTRAINT lexical_pkey PRIMARY KEY (id)
)
  WITH (
    OIDS=FALSE
);

ALTER TABLE koper.lexical_field
   OWNER TO chrono;

CREATE TABLE koper.lexical_names
(
    lexical_field_id bigserial NOT NULL,
    lexicalNames character varying
)
  WITH (
    OIDS=FALSE
);

CREATE SEQUENCE koper.lexical_ref_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

ALTER TABLE koper.lexical_ref_id_seq
     OWNER TO chrono;

