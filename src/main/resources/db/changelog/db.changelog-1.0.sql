--changeset cardManagement:1
CREATE TABLE IF NOT EXISTS "_user"
(
    user_id integer NOT NULL,
    email character varying(255) COLLATE pg_catalog."default",
    full_name character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    role character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT _user_pkey PRIMARY KEY (user_id),
    CONSTRAINT _user_role_check CHECK (role::text = ANY (ARRAY['USER'::character varying, 'ADMIN'::character varying]::text[]))
    );


CREATE TABLE IF NOT EXISTS card
(
    card_id integer NOT NULL,
    balance numeric(15,2) NOT NULL,
    card_holder_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    card_status character varying(15) COLLATE pg_catalog."default" NOT NULL,
    encrypted_card_number text COLLATE pg_catalog."default" NOT NULL,
    expiry_date date NOT NULL,
    user_id integer,
    CONSTRAINT card_pkey PRIMARY KEY (card_id),
    CONSTRAINT ukhxnba6cg3dnltuagdbrfvybpb UNIQUE (encrypted_card_number),
    CONSTRAINT fkgs9dpgxodqdjygtxyfg55ve4n FOREIGN KEY (user_id)
    REFERENCES public._user (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT card_card_status_check CHECK (card_status::text = ANY (ARRAY['ACTIVE'::character varying, 'BLOCKED'::character varying, 'EXPIRED'::character varying]::text[]))
    );



CREATE TABLE IF NOT EXISTS token
(
    id bigint NOT NULL,
    expired boolean NOT NULL,
    revoked boolean NOT NULL,
    token character varying(255) COLLATE pg_catalog."default",
    token_type character varying(255) COLLATE pg_catalog."default",
    user_id integer,
    CONSTRAINT token_pkey PRIMARY KEY (id),
    CONSTRAINT fkiblu4cjwvyntq3ugo31klp1c6 FOREIGN KEY (user_id)
    REFERENCES public._user (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT token_token_type_check CHECK (token_type::text = 'BEARER'::text)
    );
create sequence if not exists _user_seq
    increment by 50;

create sequence if not exists card_seq
    increment by 50;

create sequence if not exists token_seq
    increment by 50;
