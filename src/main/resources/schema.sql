CREATE TABLE category (
    id serial PRIMARY KEY,
    description character varying(255),
    title character varying(255),
    cnt integer
);

CREATE TABLE product (
    id serial PRIMARY KEY,
    category integer,
    description character varying(255),
    image character varying(255),
    quantity integer,
    title character varying(255),
    full_image character varying(255)
);

CREATE TABLE usr (
    id serial PRIMARY KEY,
    active boolean NOT NULL,
    password character varying(255),
    username character varying(255),
    roles character varying(255)
);

ALTER TABLE ONLY product
    ADD CONSTRAINT fkqx9wikktsev17ctu0kcpkrafc FOREIGN KEY (category) REFERENCES category(id);
