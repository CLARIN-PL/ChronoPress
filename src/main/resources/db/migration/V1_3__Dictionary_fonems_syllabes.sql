CREATE TABLE dictionary_word_fonems_syllables
(
  id serial NOT NULL,
  word character varying(255),
  fonem character varying(255),
  syllables character varying(255),
  syllableCount integer,
  fonemCount integer,
  CONSTRAINT dic_word_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);