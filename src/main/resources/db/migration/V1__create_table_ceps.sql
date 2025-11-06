CREATE TABLE ceps (
                      id SERIAL PRIMARY KEY,
                      numero_cep VARCHAR(20) NOT NULL,
                      logradouro VARCHAR(255),
                      cidade VARCHAR(100)
);