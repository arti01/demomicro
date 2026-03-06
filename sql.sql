CREATE TABLE shedlock (
      name VARCHAR(64) NOT NULL,
      lock_until TIMESTAMP NOT NULL,
      locked_at TIMESTAMP NOT NULL,
      locked_by VARCHAR(255) NOT NULL,
      PRIMARY KEY (name)
);

CREATE TABLE public.klient (
       id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
       nazwa varchar(255) NOT NULL,
       email varchar(255) NOT NULL,
       CONSTRAINT klient_pkey PRIMARY KEY (id)
);

CREATE TABLE public.transakcja (
       id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
       kwota numeric NOT NULL,
       waluta varchar(255) NOT NULL,
       klient_id int8 NOT NULL,
       CONSTRAINT transakcja_pkey PRIMARY KEY (id)
);


-- public.transakcja foreign keys
ALTER TABLE public.transakcja ADD CONSTRAINT transakcja_klient_fk FOREIGN KEY (klient_id) REFERENCES public.klient(id);