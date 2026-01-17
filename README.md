# 🚀 API de Consulta de CEP — Desafio Técnico

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-green)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![AWS](https://img.shields.io/badge/Deploy-AWS_EC2-232F3E)
![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub_Actions-2088FF)
![Status](https://img.shields.io/github/actions/workflow/status/gabrielbkx/cep-service/deploy.yml?label=Build%20%26%20Deploy)

## 🧠 Descrição

API REST desenvolvida em **Java 21** com **Spring Boot 3.5.7**, que permite consultar e gerenciar endereços por **CEP**, **logradouro** ou **cidade**.

Este projeto foi desenvolvido como parte do **desafio técnico da empresa OTI Software**, demonstrando boas práticas de **arquitetura backend**, **persistência com JPA**, **versionamento de base de dados com Flyway**, **documentação com Swagger** e **DevOps moderno**.

> 🌐 **Status do Deploy:** A aplicação está a correr em Produção numa instância **AWS EC2**, com deploy 100% automatizado via GitHub Actions.

---

## 🧩 Tecnologias Utilizadas

- ☕ **Java 21**
- ⚙️ **Spring Boot 3** (Web, Data JPA, Validation, DevTools)
- 🐘 **PostgreSQL** (Containerizado)
- 🪶 **Flyway** (Gestão de migrações da base de dados)
- 📘 **Springdoc OpenAPI / Swagger** (Documentação viva)
- 🐳 **Docker & Docker Compose**
- ☁️ **AWS EC2** (Infraestrutura de Alojamento)
- 🤖 **GitHub Actions** (Pipeline de CI/CD para Build e Deploy automáticos)

---

## 🏗️ Arquitetura e CI/CD

O projeto não só segue uma arquitetura em camadas (`Controller` ➡️ `Service` ➡️ `Repository`), como também implementa um **Pipeline de Entrega Contínua**:

1.  **Push na Main:** O código é enviado para o GitHub.
2.  **GitHub Actions:** O workflow inicia automaticamente.
    * Configura o Java e faz a Build com Maven.
    * Acede à instância AWS via SSH.
3.  **Deploy Automático:**
    * Atualiza o código na EC2.
    * Reconstrói os contentores Docker.
    * A aplicação é reiniciada com a nova versão.

---

## ⚙️ Funcionalidades e Endpoints

A API está documentada e pode ser testada diretamente pelo navegador.

🔗 **Swagger UI (Documentação Interativa):**
👉 [http://3.142.220.166:8080/swagger-ui.html](http://3.142.220.166:8080/swagger-ui.html)

### Resumo dos Endpoints

| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| `POST` | `/cep` | Cria um novo registo de CEP |
| `PUT` | `/cep` | Atualiza um CEP existente (Requer ID no corpo) |
| `GET` | `/cep/numeroCep/{numeroCep}` | Busca detalhada por número do CEP |
| `GET` | `/cep/logradouro/{logradouro}` | Lista CEPs filtrando por logradouro |
| `GET` | `/cep/cidade/{cidade}` | Lista CEPs filtrando por cidade |
| `DELETE`| `/cep/{id}` | Remove um registo de CEP |


## 🐳 Executar Localmente (Opcional)

Caso queira rodar o projeto na sua máquina local em vez de aceder à versão na nuvem:

```bash
# 1. Subir os contentores
docker compose up --build -d

---
## Acessos Locais:

API: http://localhost:8080

Swagger: http://localhost:8080/swagger-ui.html

Base de Dados: localhost:5432 

--
