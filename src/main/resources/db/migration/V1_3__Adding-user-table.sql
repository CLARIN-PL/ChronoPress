-- Table holding application properties

CREATE TABLE koper.user
(
  id bigserial NOT NULL,
  username character varying,
  password character varying,
  email character varying,
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

INSERT INTO koper.user(username, password, active) VALUES('moderator','4f785c2049363edfc0f97957b31264b40e896c6afa6639943a59376905f16843ad38aa67b6679da9', true);