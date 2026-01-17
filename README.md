# 🚀 API de Consulta de CEP — Desafio Técnico OTI Software

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-green)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![AWS](https://img.shields.io/badge/Deploy-AWS_EC2-232F3E)

## 🧠 Descrição

API REST desenvolvida em **Java 21** com **Spring Boot 3.5.7**, que permite consultar e gerenciar endereços por **CEP**, **logradouro** ou **cidade**.

Este projeto foi desenvolvido como parte do **desafio técnico da empresa OTI Software**, demonstrando boas práticas de **arquitetura backend**, **persistência com JPA**, **versionamento de banco com Flyway**, **documentação com Swagger** e **deploy em nuvem (AWS)**.

> 🌐 **Status do Deploy:** A aplicação está rodando atualmente em uma instância **AWS EC2 (Ubuntu)** orquestrada via Docker Compose.

---

## 🧩 Tecnologias Utilizadas

- ☕ **Java 21**
- ⚙️ **Spring Boot 3** (Web, Data JPA, Validation, DevTools)
- 🐘 **PostgreSQL** (Containerizado)
- 🪶 **Flyway** (Gerenciamento de migrações de banco de dados)
    - *Nota: A stack sugerida incluía Liquibase, mas optou-se pelo Flyway devido à familiaridade e robustez.*
- 📘 **Springdoc OpenAPI / Swagger** (Documentação viva)
- 💡 **Lombok** (Redução de boilerplate)
- 🐳 **Docker & Docker Compose**
- ☁️ **AWS EC2** (Infraestrutura de Hospedagem)

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas bem definidas para garantir a **separação de responsabilidades** e fácil manutenção:

`Controller` ➡️ `Service` ➡️ `Repository (Data Access)` ➡️ `Database`

---

## ⚙️ Funcionalidades e Endpoints

A API está documentada e pode ser testada diretamente pelo navegador.

🔗 **Swagger UI (Documentação Interativa):**
👉 [http://3.142.220.166:8080/swagger-ui.html](http://3.142.220.166:8080/swagger-ui.html)

### Resumo dos Endpoints

| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| `POST` | `/cep` | Cria um novo registro de CEP |
| `PUT` | `/cep` | Atualiza um CEP existente (Requer ID no corpo) |
| `GET` | `/cep/numeroCep/{numeroCep}` | Busca detalhada por número do CEP |
| `GET` | `/cep/logradouro/{logradouro}` | Lista CEPs filtrando por logradouro |
| `GET` | `/cep/cidade/{cidade}` | Lista CEPs filtrando por cidade |
| `DELETE`| `/cep/{id}` | Remove um registro de CEP |

---

## 🧪 Como Testar (Front-end de Teste)

Para facilitar a validação dos endpoints sem necessidade de ferramentas como Postman, foi desenvolvido um **Front-end SPA (Single Page Application)** simples.

1. Baixe o arquivo `index.html` (e seus arquivos `.css` e `.js` anexos no repositório).
2. Abra no seu navegador.
3. O front-end já está configurado para consumir a API rodando na AWS.

---

## 🧱 Banco de Dados e Migrations

- **Banco:** PostgreSQL 15 (Docker)
- **Gerenciador:** Flyway
- **Schema Inicial:** Definido em `V1__create_table_ceps.sql`

```sql
CREATE TABLE ceps (
    id SERIAL PRIMARY KEY,
    numero_cep VARCHAR(20) NOT NULL,
    logradouro VARCHAR(255) NOT NULL,
    cidade VARCHAR(100) NOT NULL
);

## 🐳 Executando Localmente (Docker)

**docker compose up --build -d**

2️⃣ Acesso Local

API: http://localhost:8080

Swagger: http://localhost:8080/swagger-ui.html

Banco: localhost:5432

3️⃣ Parar a execução
docker compose down

Sim, Gabriel, entendi perfeitamente! Você pegou um desafio técnico, dockerizou, subiu na nuvem (AWS EC2), configurou o banco e agora tem até um front-end para consumir. Isso valoriza muito o seu portfólio.

Para atualizar a documentação (README.md), precisamos refletir esse ambiente de Produção. Um recrutador ou avaliador técnico vai gostar de ver que o projeto não roda apenas na "sua máquina", mas está vivo na internet.

Abaixo está uma versão profissional e atualizada do seu README. Fiz as seguintes melhorias:

Status de Deploy: Destaquei que está rodando na AWS.

URLs Reais: Substituí localhost pelo IP da sua instância.

Correção de Endpoints: Ajustei a tabela de endpoints baseada no Swagger que você me mandou (ex: /cep/numeroCep/... em vez de /cep/...).

Inclusão do Front-end: Mencionei que existe uma interface visual (o HTML que criamos) para facilitar os testes deles.

Aqui está o código Markdown pronto para copiar e colar no seu repositório:

Markdown
# 🚀 API de Consulta de CEP — Desafio Técnico OTI Software

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-green)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![AWS](https://img.shields.io/badge/Deploy-AWS_EC2-232F3E)

## 🧠 Descrição

API REST desenvolvida em **Java 21** com **Spring Boot 3.5.7**, que permite consultar e gerenciar endereços por **CEP**, **logradouro** ou **cidade**.

Este projeto foi desenvolvido como parte do **desafio técnico da empresa OTI Software**, demonstrando boas práticas de **arquitetura backend**, **persistência com JPA**, **versionamento de banco com Flyway**, **documentação com Swagger** e **deploy em nuvem (AWS)**.

> 🌐 **Status do Deploy:** A aplicação está rodando atualmente em uma instância **AWS EC2 (Ubuntu)** orquestrada via Docker Compose.

---

## 🧩 Tecnologias Utilizadas

- ☕ **Java 21**
- ⚙️ **Spring Boot 3** (Web, Data JPA, Validation, DevTools)
- 🐘 **PostgreSQL** (Containerizado)
- 🪶 **Flyway** (Gerenciamento de migrações de banco de dados)
    - *Nota: A stack sugerida incluía Liquibase, mas optou-se pelo Flyway devido à familiaridade e robustez.*
- 📘 **Springdoc OpenAPI / Swagger** (Documentação viva)
- 💡 **Lombok** (Redução de boilerplate)
- 🐳 **Docker & Docker Compose**
- ☁️ **AWS EC2** (Infraestrutura de Hospedagem)

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas bem definidas para garantir a **separação de responsabilidades** e fácil manutenção:

`Controller` ➡️ `Service` ➡️ `Repository (Data Access)` ➡️ `Database`

---

## ⚙️ Funcionalidades e Endpoints

A API está documentada e pode ser testada diretamente pelo navegador.

🔗 **Swagger UI (Documentação Interativa):**
👉 [http://3.142.220.166:8080/swagger-ui.html](http://3.142.220.166:8080/swagger-ui.html)

### Resumo dos Endpoints

| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| `POST` | `/cep` | Cria um novo registro de CEP |
| `PUT` | `/cep` | Atualiza um CEP existente (Requer ID no corpo) |
| `GET` | `/cep/numeroCep/{numeroCep}` | Busca detalhada por número do CEP |
| `GET` | `/cep/logradouro/{logradouro}` | Lista CEPs filtrando por logradouro |
| `GET` | `/cep/cidade/{cidade}` | Lista CEPs filtrando por cidade |
| `DELETE`| `/cep/{id}` | Remove um registro de CEP |

---

## 🧪 Como Testar (Front-end de Teste)

Para facilitar a validação dos endpoints sem necessidade de ferramentas como Postman, foi desenvolvido um **Front-end SPA (Single Page Application)** simples.

1. Baixe o arquivo `index.html` (e seus arquivos `.css` e `.js` anexos no repositório).
2. Abra no seu navegador.
3. O front-end já está configurado para consumir a API rodando na AWS.

---

## 🧱 Banco de Dados e Migrations

- **Banco:** PostgreSQL 15 (Docker)
- **Gerenciador:** Flyway
- **Schema Inicial:** Definido em `V1__create_table_ceps.sql`

```sql
CREATE TABLE ceps (
    id SERIAL PRIMARY KEY,
    numero_cep VARCHAR(20) NOT NULL,
    logradouro VARCHAR(255) NOT NULL,
    cidade VARCHAR(100) NOT NULL
);

## 🐳 Executando Localmente (Docker)

Caso queira rodar o projeto em sua máquina local em vez de acessar a versão na nuvem:

### 1️⃣ Build e Subida dos Containers
```bash
docker compose up --build -d

## 2️⃣ Acesso Local

- **API: http://localhost:8080

- **Swagger: http://localhost:8080/swagger-ui.html

- **Banco: localhost:5432`

## 3️⃣ Parar a execução
docker compose down´

🧑‍💻 Autor
Gabriel de Oliveira Ferreira 💼 Desenvolvedor Backend Java

📧 oliveirafrerreira97@hotmail.com

🌐 LinkedIn

🏁 Status do Projeto
✅ Em Desenvolvimento — API funcional, documentada, dockerizada e hospedada na AWS.
