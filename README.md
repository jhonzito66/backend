# 🌾 Backend - Agrotask | Sistema de Gestão de Tarefas para Propriedades Rurais

Bem-vindo ao repositório do **Agrotask**, um sistema completo para **gestão de tarefas em propriedades rurais**, desenvolvido para apoiar **proprietários e gerentes** no manejo eficiente de suas atividades agrícolas. A plataforma permite controle detalhado de **tarefas**, **insumos** e **maquinários**, com autenticação segura e integração entre usuários.

---

## 🚜 Visão Geral

O Agrotask oferece uma solução prática para o dia a dia do campo, permitindo que tarefas sejam atribuídas, monitoradas e finalizadas com controle de recursos. A aplicação é estruturada com:

- **Backend** em Java (Spring Boot)
- **Banco de dados relacional PostgreSQL**
- **Supabase** como **backend adicional obrigatório** para autenticação, persistência remota e funcionalidades em tempo real
- **Frontend** em JavaFX (interface para desktop)

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.x**
- **Spring Security** – Autenticação e autorização com JWT
- **Spring Data JPA** e **Hibernate** – Persistência local
- **PostgreSQL** – Banco relacional principal
- **Supabase** – Autenticação, persistência e sincronização em tempo real
- **Lombok** – Redução de código repetitivo
- **Maven** – Gerenciamento de dependências

---

## 🔐 Autenticação com Supabase

A autenticação dos usuários é feita por meio do **Supabase Auth**, que fornece um backend seguro e moderno com tokens JWT, e está completamente integrado ao fluxo de login e registro da aplicação.

- Cada usuário registrado no sistema é também sincronizado com a base do Supabase
- Tokens JWT emitidos pelo Supabase são utilizados nas rotas seguras do sistema
- Os dados críticos também podem ser sincronizados via Supabase para redundância ou operação remota

---

## ✨ Funcionalidades Principais

### 👥 Usuários

- Registro e login via Supabase Auth
- Perfis:
  - `ADMIN`: Controle total (**Proprietário**)
  - `MODERADOR`: Gestão local (**Gerente**)
  - `COMUM`: Execução de tarefas (**Peão**)

### 📋 Tarefas Rurais

- Criação e atribuição de tarefas com prazo
- Controle de status: `PENDENTE`, `EM_ANDAMENTO`, `CONCLUIDA`
- Registro de observações na finalização

### 🧪 Insumos

- Cadastro de insumos (fertilizantes, sementes, agrotóxicos, etc.)
- Controle de estoque e consumo por tarefa
- A relação com tarefas é registrada na tabela `tarefa_insumo`

### 🚜 Maquinários

- Registro de equipamentos como tratores, pulverizadores, colheitadeiras
- Controle de status (`DISPONIVEL`, `EM_USO`, `MANUTENCAO`)
- Histórico de lavagem e abastecimento por tarefa (registrado na `tarefa_maquinario`)

---

## ⚙️ Configuração do Ambiente

### ✅ Pré-requisitos

- JDK 21+
- Maven
- PostgreSQL rodando localmente
- Conta e projeto ativo no [Supabase](https://supabase.com)

---

### 🔽 Clonar o Repositório

```bash
git clone <URL_DO_REPOSITORIO>
cd backend
```

---

### 🛠️ Configurar Banco de Dados e Supabase

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.application.name=backend
server.port=8080

# Conexão com o Supabase
spring.datasource.url=jdbc:postgresql://db.sdynvhglywvomrnpzaex.supabase.co:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=Jao1853242@
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Logging SQL detalhado
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# CORS
spring.web.cors.allowed-origins=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
spring.web.cors.max-age=3600
```

📌 **Importante**: Substitua `SEGREDO_JWT_DO_SUPABASE` pelo segredo fornecido no seu Supabase (`JWT_SECRET` nas configurações de autenticação).

---

### ▶️ Executando o Projeto

No terminal, a partir da raiz do projeto:

```bash
mvn spring-boot:run
```

A aplicação ficará acessível em: [http://localhost:8080](http://localhost:8080)

---

## 📡 Principais Endpoints da API

### 🔐 Autenticação (`/api/auth`)
- **POST** `/register`: Registro no Supabase e base local
- **POST** `/login`: Valida credenciais e retorna JWT (via Supabase)

### 🧾 Tarefas (`/api/tarefas`)
- **POST** `/`: Criar nova tarefa
- **GET** `/`: Listar tarefas
- **PUT** `/{id}/status/{status}`: Alterar status
- **PUT** `/{id}/finalizar`: Finalizar tarefa com insumos/maquinários

### 🧪 Insumos (`/api/tarefas/insumos`)
- **POST** `/`: Cadastrar insumo
- **GET** `/`: Listar insumos
- **PUT** `/{id}`: Editar insumo
- **DELETE** `/{id}`: Remover insumo

### 🚜 Maquinários (`/api/tarefas/maquinarios`)
- **POST** `/`: Cadastrar maquinário
- **GET** `/`: Listar maquinários
- **PUT** `/{id}`: Atualizar maquinário
- **DELETE** `/{id}`: Excluir maquinário

### 🛡️ Todos os endpoints protegidos exigem o token JWT (do Supabase) no cabeçalho

---

## 🗃️ Banco de Dados

O banco de dados relacional utilizado é o PostgreSQL, com tabelas criadas automaticamente via JPA. A estrutura do modelo representa fielmente o funcionamento de uma fazenda moderna.

### 📂 Tabelas e Relações

- **Usuários (`usuarios`)**
  - ADMIN → Proprietário
  - MODERADOR → Gerente
  - COMUM → Peão

- **Tarefas (`tarefas`)**
  - Atribuídas por um usuário a outro
  - Possuem status, prazos e conclusão com observações

- **Insumos (`insumos`)**
  - Itens consumíveis como sementes, fertilizantes, defensivos
  - Controle de validade e quantidade
  - Associados às tarefas via tabela `tarefa_insumo`

- **Maquinários (`maquinarios`)**
  - Equipamentos como tratores, colheitadeiras, pulverizadores
  - Estado de uso controlado por status
  - Informações de lavagem/abastecimento registradas na `tarefa_maquinario`

---

## 🖥️ Frontend

Este backend se integra a uma aplicação JavaFX feita para funcionar localmente em zonas rurais com baixa ou nenhuma conectividade.

🔗 Repositório do Frontend Agrotask: [github.com/jhonzito66/frontend](https://github.com/jhonzito66/frontend)
