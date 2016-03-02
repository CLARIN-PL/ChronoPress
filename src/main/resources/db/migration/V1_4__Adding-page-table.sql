-- Table Aggregates pages for education module
CREATE TABLE koper.page_aggregator
(
    id bigserial NOT NULL,
    title character varying,

    CONSTRAINT pageAggregator_pkey PRIMARY KEY (id)
)
  WITH (
    OIDS=FALSE
);

ALTER TABLE koper.page_aggregator
   OWNER TO chrono;

CREATE SEQUENCE koper.pageAggregator_ref_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
  ALTER TABLE koper.pageAggregator_ref_id_seq
    OWNER TO chrono;

-- Table holding pages for education module

CREATE TABLE koper.page
(
  id bigserial NOT NULL,
  title character varying,
  content text,
  published boolean,
  page_aggregator_id bigint,

  CONSTRAINT page_pkey PRIMARY KEY (id),
  CONSTRAINT fk_page_aggregator FOREIGN KEY (page_aggregator_id)
        REFERENCES  koper.page_aggregator (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

ALTER TABLE koper.page
  OWNER TO chrono;

CREATE SEQUENCE koper.page_ref_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE koper.page_ref_id_seq
  OWNER TO chrono;



