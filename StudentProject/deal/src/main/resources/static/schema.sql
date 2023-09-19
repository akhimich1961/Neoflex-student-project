-- Database: dbcredit

DROP DATABASE IF EXISTS dbcredit;

CREATE DATABASE dbcredit
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- GRANT TEMPORARY, CONNECT ON DATABASE dbcredit TO PUBLIC;

GRANT ALL ON DATABASE dbcredit TO postgres;

GRANT ALL ON DATABASE dbcredit TO springuser;


\connect dbcredit

DROP TABLE IF EXISTS public.credit CASCADE;
DROP TABLE IF EXISTS public.client CASCADE;
DROP TABLE IF EXISTS public.application CASCADE;



-- Table: public.client


CREATE TABLE IF NOT EXISTS public.client
(
    client_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    last_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    first_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    middle_name character varying(255) COLLATE pg_catalog."default",
    birth_date date NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    gender character varying(255) COLLATE pg_catalog."default",
    marital_status character varying(255) COLLATE pg_catalog."default",
    dependent_amount integer,
    passport jsonb NOT NULL,
    employment jsonb,
    account character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT client_pkey PRIMARY KEY (client_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.client
    OWNER to postgres;

GRANT ALL ON TABLE public.client TO postgres;

GRANT ALL ON TABLE public.client TO springuser;



-- Table: public.credit


CREATE TABLE IF NOT EXISTS public.credit
(
    credit_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    amount numeric NOT NULL,
    term integer NOT NULL,
    monthly_payment numeric NOT NULL,
    rate numeric NOT NULL,
    psk numeric NOT NULL,
    payment_schedule jsonb NOT NULL,
    insurance_enable boolean NOT NULL,
    salary_client boolean NOT NULL,
    credit_status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT credit_pkey PRIMARY KEY (credit_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.credit
    OWNER to postgres;

GRANT ALL ON TABLE public.credit TO postgres;

GRANT ALL ON TABLE public.credit TO springuser;



-- Table: public.application


CREATE TABLE IF NOT EXISTS public.application
(
    application_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    client_id bigint NOT NULL,
    credit_id bigint,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    creation_date timestamp(6) without time zone NOT NULL,
    applied_offer jsonb,
    sign_date timestamp(6) without time zone,
    ses_code integer,
    status_history jsonb NOT NULL,
    CONSTRAINT application_pkey PRIMARY KEY (application_id),
    CONSTRAINT client_id FOREIGN KEY (client_id)
        REFERENCES public.client (client_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT credit_id FOREIGN KEY (credit_id)
        REFERENCES public.credit (credit_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.application OWNER to postgres;

GRANT ALL ON TABLE public.application TO postgres;

GRANT ALL ON TABLE public.application TO springuser;
