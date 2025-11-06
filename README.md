# 🚀 API de Consulta de CEP — Desafio Técnico OTI Software

## 🧠 Descrição

API REST desenvolvida em **Java 21** com **Spring Boot 3.5.7**, que permite consultar e gerenciar endereços por
**CEP**, **logradouro** ou **cidade**.  
Este projeto foi desenvolvido como parte do **desafio técnico da empresa OTI Software**,
demonstrando boas práticas de **arquitetura backend**, **persistência com JPA**, **versionamento de banco com Flyway**,
**documentação com Swagger** e **deploy em containers Docker**.

---

## 🧩 Tecnologias Utilizadas

- ☕ **Java 21**
- ⚙️ **Spring Boot 3 (Web, Data JPA, Validation,DevTools)**
- 🐘 **PostgreSQL** (via Docker Compose)
- 🪶 **Flyway** (migrations do banco) - **O opcional era liquibase mas ainda nao conheço, o flyway ja tenho um costume**
- 📘 **Swagger / Springdoc OpenAPI** (documentação dos endpoints) - **Opcional**
- 💡 **Lombok** (para reduzir boilerplate)
- 🐳 **Docker e Docker Compose**

---

## 🏗️ Arquitetura da Aplicação

O projeto segue uma arquitetura em camadas padrao Controller, Repository(DAO) e Service e DTOs para **separação de responsabilidades**:


---

## ⚙️ Funcionalidades

### Endpoints Principais

| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| `GET` | `/cep/{numeroCep}` | Retorna o CEP específico |
| `GET` | `/cep/logradouro/{logradouro}` | Lista de CEPs por logradouro |
| `GET` | `/cep/cidade/{cidade}` | Lista de CEPs por cidade |
| `POST` | `/ceps` | Cria um novo CEP |
| `PUT` | `/cep/{id}` | Atualiza um CEP existente |

📄 **Documentação Swagger disponível em:**  
👉 [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)

---

## 🧱 Banco de Dados e Migrations

- Banco: **PostgreSQL**
- Migrations: **Flyway**
- Tabela inicial: `ceps`

Exemplo de migration (`V1__create_table_ceps.sql`):

```sql
CREATE TABLE ceps (
    id SERIAL PRIMARY KEY,
    numero_cep VARCHAR(20) NOT NULL,
    logradouro VARCHAR(255) NOT NULL,
    cidade VARCHAR(100) NOT NULL
);
```
🐳 Dockerização

O ambiente completo roda em containers via Docker Compose.

🧰 Comandos principais

1️⃣ Build das imagens
```
docker compose build

```
2️⃣ Subir os containers
```
docker compose up

```

3️⃣ Rodar em segundo plano
```
docker compose up -d

```
4️⃣ Encerrar tudo
```
docker compose down

```
Após subir, a API estará disponívei em:

API: http://localhost:8080

Banco: localhost:5432


🧪 Teste da Aplicação
```
Exemplo de requisição para criar um CEP (via Postman ou cURL):
POST http://localhost:8080/cep
Content-Type: application/json

{
  "numeroCep": "28940000",
  "logradouro": "Rua das Flores",
  "cidade": "Araruama"
}
```
🧾 Observações

O projeto utiliza o perfil prod ao rodar no Docker.

O banco de dados é criado automaticamente com as migrations Flyway.

Todo o ambiente (API + banco) é orquestrado via Docker Compose, sem necessidade de configurações manuais.

## 🧑‍💻 Autor

**Gabriel de Oliveira Ferreira**  
💼 Desenvolvedor Backend Java  
📧 oliveirafrerreira97@hotmail.com  
🌐 [LinkedIn](https://www.linkedin.com/in/gabriel-oliveira-ferreira/)

🏁 Status do Projeto

✅ Concluído com sucesso — Projeto funcional, documentado e dockerizado.