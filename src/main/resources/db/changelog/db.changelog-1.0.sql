-- changeset cardManagement:1

-- Table: _user
CREATE TABLE IF NOT EXISTS "_user" (
                                       user_id integer NOT NULL,
                                       email character varying(255),
    full_name character varying(255),
    password character varying(255),
    role character varying(255),
    CONSTRAINT _user_pkey PRIMARY KEY (user_id),
    CONSTRAINT _user_role_check CHECK (role IN ('USER', 'ADMIN'))
    );

-- Table: card
CREATE TABLE IF NOT EXISTS card (
                                    card_id integer NOT NULL,
                                    balance numeric(15,2) NOT NULL,
    card_holder_name character varying(100) NOT NULL,
    card_status character varying(15) NOT NULL,
    encrypted_card_number text NOT NULL,
    expiry_date date NOT NULL,
    user_id integer,
    CONSTRAINT card_pkey PRIMARY KEY (card_id),
    CONSTRAINT ukhxnba6cg3dnltuagdbrfvybpb UNIQUE (encrypted_card_number),
    CONSTRAINT fkgs9dpgxodqdjygtxyfg55ve4n FOREIGN KEY (user_id)
    REFERENCES "_user" (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT card_card_status_check CHECK (card_status IN ('ACTIVE', 'BLOCKED', 'EXPIRED'))
    );

-- Table: token
CREATE TABLE IF NOT EXISTS token (
                                     id bigint NOT NULL,
                                     expired boolean NOT NULL,
                                     revoked boolean NOT NULL,
                                     token character varying(255),
    token_type character varying(255),
    user_id integer,
    CONSTRAINT token_pkey PRIMARY KEY (id),
    CONSTRAINT fkiblu4cjwvyntq3ugo31klp1c6 FOREIGN KEY (user_id)
    REFERENCES "_user" (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT token_token_type_check CHECK (token_type = 'BEARER')
    );

-- Sequences
CREATE SEQUENCE IF NOT EXISTS _user_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS card_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS token_seq START WITH 1 INCREMENT BY 50;
