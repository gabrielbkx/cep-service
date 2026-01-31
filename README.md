# 🚀 API de Consulta de CEP — Desafio Técnico

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-green)
![Security](https://img.shields.io/badge/Spring_Security-JWT-red)
![Tests](https://img.shields.io/badge/Tests-JUnit5_&_Mockito-brightgreen)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![AWS](https://img.shields.io/badge/Deploy-AWS_EC2-232F3E)
![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub_Actions-2088FF)
![Status](https://img.shields.io/github/actions/workflow/status/gabrielbkx/cep-service/deploy.yml?label=Build%20%26%20Deploy)

## 🧠 Descrição

API REST segura e robusta desenvolvida em **Java 21** com **Spring Boot 3.5.7**, que permite consultar e gerir endereços por **CEP**, **logradouro** ou **cidade**.

Este projeto foi desenvolvido como parte do **desafio técnico da empresa OTI Software**, demonstrando boas práticas de:
* **Arquitetura Backend Sólida** (Camadas e DTOs).
* **Segurança Stateless** (Autenticação JWT e Controlo de Acesso RBAC).
* **Qualidade de Código** (Testes Unitários com Mockito).
* **DevOps Moderno** (Docker, CI/CD e Cloud AWS).
* **Tratamento Centralizado de Erros** (Global Exception Handling).

> 🌐 **Status do Deploy:** A aplicação está a correr em Produção numa instância **AWS EC2**, com deploy 100% automatizado via GitHub Actions.

---

## 🧩 Tecnologias Utilizadas

- ☕ **Java 21**
- ⚙️ **Spring Boot 3** (Web, Data JPA, Validation, Security)
- 🔒 **Spring Security & Auth0 Java-JWT** (Autenticação e Autorização)
- 🧪 **JUnit 5 & Mockito** (Testes Unitários)
- 🐘 **PostgreSQL** (Containerizado)
- 🪶 **Flyway** (Gestão de migrações da base de dados)
- 📘 **Springdoc OpenAPI / Swagger** (Documentação viva)
- 🐳 **Docker & Docker Compose**
- ☁️ **AWS EC2** (Infraestrutura de Alojamento)
- 🤖 **GitHub Actions** (Pipeline de CI/CD)

---

## 🏗️ Arquitetura e Segurança

O projeto segue uma arquitetura em camadas protegida por filtros de segurança:

1.  **Request:** O cliente envia a requisição HTTP.
2.  **Security Filter:** Intercepta a requisição, valida o **Token JWT** e define as permissões (USER ou ADMIN).
3.  **Controller:** Recebe a requisição se autenticada.
4.  **Service:** Executa a regra de negócio (com suporte de transações).
5.  **Repository:** Interage com a base de dados.
6.  **Exception Handler:** Captura falhas (401, 403, 404) e devolve respostas JSON padronizadas.

### 🛡️ Autenticação e Permissões
A API utiliza **Tokens JWT (JSON Web Tokens)**.
* **ADMIN:** Acesso total (inclui exclusão de registos).
* **USER:** Acesso de leitura e criação/edição.
* **ANÓNIMO:** Apenas acesso aos endpoints de Login e Cadastro.

---

## ⚙️ Funcionalidades e Endpoints

A API está documentada e pode ser testada diretamente pelo navegador.
*Nota: Para testar endpoints protegidos no Swagger, é necessário autenticar-se no botão "Authorize" com o token recebido no login.*

🔗 **Swagger UI (Documentação Interativa):**
👉 [http://3.142.220.166:8080/swagger-ui.html](http://3.142.220.166:8080/swagger-ui.html)

### 🔐 Autenticação (Público)

| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| `POST` | `/auth/login` | Autentica o utilizador e devolve o **Token JWT** |
| `POST` | `/auth/cadastrar` | Regista um novo utilizador no sistema |

### 📍 Endereços (Protegido)

| Método | Endpoint | Permissão | Descrição |
|--------|-----------|------------|-----------|
| `POST` | `/cep` | 🔒 USER/ADMIN | Cria um novo registo de CEP |
| `PUT` | `/cep` | 🔒 USER/ADMIN | Atualiza um CEP existente |
| `GET` | `/cep/numeroCep/{numeroCep}` | 🔒 USER/ADMIN | Busca detalhada por número do CEP |
| `GET` | `/cep/logradouro/{logradouro}` | 🔒 USER/ADMIN | Lista CEPs por logradouro |
| `GET` | `/cep/cidade/{cidade}` | 🔒 USER/ADMIN | Lista CEPs por cidade |
| `DELETE`| `/cep/{id}` | 🔒 **ADMIN** | Remove um registo de CEP |

credenciais admin: 
usuario: admin
senha: advinha kkk

---

## 🧪 Testes

O projeto conta com cobertura de testes unitários utilizando **JUnit 5** e **Mockito*

## 🐳 Executar Localmente 

Caso queira rodar o projeto na sua máquina local em vez de aceder à versão na nuvem:

```bash
# 1. Subir os contentores
docker compose up --build -d

---
## Acessos Locais:

API: http://localhost:8080

Swagger: http://localhost:8080/swagger-ui.html


---
