CREATE TABLE ceps (
                      id BIGSERIAL PRIMARY KEY,
                      numero_cep VARCHAR(20) NOT NULL,
                      logradouro VARCHAR(255),
                      cidade VARCHAR(100)
);


CREATE TABLE USUARIOS (
                          ID BIGSERIAL PRIMARY KEY ,
                          USUARIO VARCHAR(100) NOT NULL UNIQUE,
                          EMAIL VARCHAR(100) NOT NULL UNIQUE,
                          SENHA VARCHAR(100) NOT NULL
);