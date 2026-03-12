# 🚀 API de Consulta de CEP — Desafio Técnico

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-green)
![Security](https://img.shields.io/badge/Spring_Security-JWT-red)
![Tests](https://img.shields.io/badge/Tests-JUnit5_&_Mockito-brightgreen)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![Render](https://img.shields.io/badge/Deploy-Render-46E3B7)
![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub_Actions-2088FF)
![Status](https://img.shields.io/github/actions/workflow/status/gabrielbkx/cep-service/maven.yml?label=Build%20%26%20Tests)

## 🧠 Descrição

API REST segura e robusta desenvolvida em **Java 21** com **Spring Boot 3.5.7**, que permite consultar e gerir endereços por **CEP**, **logradouro** ou **cidade**.

Este projeto foi desenvolvido como parte do **desafio técnico da empresa OTI Software**, demonstrando boas práticas de:
* **Arquitetura Backend Sólida** (Camadas e DTOs).
* **Segurança Stateless** (Autenticação JWT e Controlo de Acesso RBAC).
* **Qualidade de Código** (Testes Unitários com Mockito).
* **DevOps Moderno** (Docker, CI/CD e Cloud).
* **Tratamento Centralizado de Erros** (Global Exception Handling).

> 🌐 **Status do Deploy:** A aplicação está em produção na plataforma **Render**, com deploy 100% automatizado via GitHub Actions.

---

## 🧩 Tecnologias Utilizadas

- ☕ **Java 21**
- ⚙️ **Spring Boot 3** (Web, Data JPA, Validation, Security)
- 🔒 **Spring Security & Auth0 Java-JWT** (Autenticação e Autorização)
- 🧪 **JUnit 5 & Mockito** (Testes Unitários)
- 🐘 **PostgreSQL** (Banco de dados em produção)
- 🪶 **Liquibase** (Gestão de migrações do banco de dados)
- 📘 **Springdoc OpenAPI / Swagger** (Documentação interativa)
- 🐳 **Docker & Docker Compose**
- ☁️ **Render** (Infraestrutura de hospedagem)
- 🤖 **GitHub Actions** (Pipeline de CI/CD)

---

## 🏗️ Arquitetura e Segurança

O projeto segue uma arquitetura em camadas protegida por filtros de segurança:

1. **Request:** O cliente envia a requisição HTTP.
2. **Security Filter:** Intercepta a requisição, valida o **Token JWT** e define as permissões (USER ou ADMIN).
3. **Controller:** Recebe a requisição se autenticada.
4. **Service:** Executa a regra de negócio (com suporte de transações).
5. **Repository:** Interage com o banco de dados.
6. **Exception Handler:** Captura falhas (401, 403, 404) e devolve respostas JSON padronizadas.

### 🛡️ Autenticação e Permissões
A API utiliza **Tokens JWT (JSON Web Tokens)**.
* **ADMIN:** Acesso total (inclui exclusão de registros).
* **USER:** Acesso de leitura e criação/edição.
* **ANÔNIMO:** Apenas acesso aos endpoints de Login e Cadastro.

---

## ⚙️ Funcionalidades e Endpoints

A API está documentada e pode ser testada diretamente pelo navegador.

*Nota: Para testar endpoints protegidos no Swagger, é necessário autenticar-se no botão "Authorize" com o token recebido no login.*

🔗 **Swagger UI (Documentação Interativa):**
👉 [https://cep-service.onrender.com/swagger-ui.html](https://cep-service.onrender.com/swagger-ui.html)

### 🔐 Autenticação (Público)

| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| `POST` | `/auth/login` | Autentica o usuário e retorna o **Token JWT** |
| `POST` | `/auth/cadastrar` | Registra um novo usuário no sistema |

### 📍 Endereços (Protegido)

| Método | Endpoint | Permissão | Descrição |
|--------|-----------|------------|-----------|
| `POST` | `/cep` | 🔒 USER/ADMIN | Cria um novo registro de CEP |
| `PUT` | `/cep` | 🔒 USER/ADMIN | Atualiza um CEP existente |
| `GET` | `/cep/numeroCep/{numeroCep}` | 🔒 USER/ADMIN | Busca detalhada por número do CEP |
| `GET` | `/cep/logradouro/{logradouro}` | 🔒 USER/ADMIN | Lista CEPs por logradouro |
| `GET` | `/cep/cidade/{cidade}` | 🔒 USER/ADMIN | Lista CEPs por cidade |
| `DELETE`| `/cep/{id}` | 🔒 **ADMIN** | Remove um registro de CEP |

---

## 🧪 Testes

O projeto conta com cobertura de testes unitários utilizando **JUnit 5** e **Mockito**. O pipeline de CI no GitHub Actions executa todos os testes automaticamente a cada push na branch `main`.

---

## 🐳 Executar Localmente

Caso queira rodar o projeto na sua máquina local em vez de acessar a versão na nuvem:

```bash
# Subir os contêineres
docker compose up --build -d
```

### Acessos Locais

- **API:** http://localhost:8080
- **Swagger:** http://localhost:8080/swagger-ui.html
