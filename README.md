# 🤖 MCP Java Service

A Spring Boot service that exposes a chat API for a UI, runs GraphQL queries over company employee data, and uses Spring AI tool-calling so plain-English prompts can be answered through GraphQL-backed MCP tools.

## ✨ What It Does

- Accepts natural-language prompts like `show female employees in engineering`
- Uses Spring AI + OpenAI to decide which backend MCP tool to call
- Uses GraphQL as the flexible query layer for employees and departments
- Exposes a Swagger-testable REST bridge for GraphQL queries
- Connects to PostgreSQL
- Initializes schema and sample data idempotently on startup

## 🧱 Stack

- Java 21
- Gradle
- Spring Boot 3.2
- Spring Web MVC
- Spring Data JPA
- Spring for GraphQL
- Spring AI
- OpenAI model starter
- Spring AI MCP Server (WebMVC)
- PostgreSQL
- Springdoc OpenAPI / Swagger UI

## 🗂️ Project Shape

```text
src/main/java/com/example/mcpjavaservice
src/main/java/com/example/mcp
src/main/resources
src/main/resources/graphql
src/test/java
.vscode
.env
```

## 🔌 HTTP Endpoints

Base path:

```text
/mcpservice
```

### Chat

```http
POST /mcpservice/api/chat
Content-Type: application/json
```

Request body:

```json
{
  "message": "Give me all employees for department Engineering"
}
```

Response body:

```json
{
  "reply": "Engineering has 30 employees ..."
}
```

### Swagger-Testable GraphQL Bridge

```http
POST /mcpservice/api/graphql
Content-Type: application/json
```

Request body:

```json
{
  "query": "query($department: String) { employees(filter: { departmentName: $department }) { employeeId name age gender } }",
  "operationName": null,
  "variables": {
    "department": "Engineering"
  }
}
```

### Native GraphQL Endpoint

```http
POST /mcpservice/graphql
Content-Type: application/json
```

## 🧠 How The Chat Flow Works

1. The UI sends a natural-language prompt to `/api/chat`.
2. `ChatService` builds a Spring AI `ChatClient` request.
3. Spring AI exposes `CompanyMcpGraphQlTools` as callable MCP tools.
4. The model selects the most relevant tool based on the user request.
5. The selected MCP tool executes a GraphQL query through `GraphQlSource`.
6. GraphQL resolves the query via `CompanyGraphQlController`.
7. The GraphQL controller uses `EmployeeService` and `DepartmentService`.
8. The backend returns the final assistant reply to the UI.

## 🛠️ MCP Tool Layers

### Primary Tool Layer For Chat

Defined in [src/main/java/com/example/mcp/CompanyMcpGraphQlTools.java](/workspaces/mcpjavaservice/src/main/java/com/example/mcp/CompanyMcpGraphQlTools.java):

- `graphqlEmployees`
- `graphqlDepartments`
- `graphqlCountEmployeesByDepartment`

These tools execute GraphQL queries internally and are the preferred tool surface for chat and LLM flows.

## 🧬 GraphQL Queries Supported

GraphQL schema is defined in [src/main/resources/graphql/schema.graphqls](/workspaces/mcpjavaservice/src/main/resources/graphql/schema.graphqls).

Main queries:

- `employees(filter: EmployeeFilterInput)`
- `departments(filter: DepartmentFilterInput)`
- `countEmployeesByDepartment(filter: DepartmentFilterInput)`

Example GraphQL queries:

```graphql
query {
  employees(filter: { departmentName: "Engineering" }) {
    employeeId
    name
    age
    gender
  }
}
```

```graphql
query {
  employees(filter: { minAge: 41 }) {
    name
    age
  }
}
```

```graphql
query {
  employees(filter: { departmentName: "Engineering", gender: "Female" }) {
    name
    departments {
      departmentName
    }
  }
}
```

```graphql
query {
  departments {
    departmentName
    employeeCount
    employees {
      name
      age
    }
  }
}
```

```graphql
query {
  departments(filter: { employeeName: "John" }) {
    departmentName
    employees {
      name
    }
  }
}
```

```graphql
query {
  countEmployeesByDepartment {
    departmentName
    employeeCount
  }
}
```

## 🗃️ Database

The service uses PostgreSQL:

- Host: `host.docker.internal`
- Port: `5432`
- Database: `mcpdb`
- User: `mcpuser`

Configured in [src/main/resources/application.yml](/workspaces/mcpjavaservice/src/main/resources/application.yml).

### Schema Initialization

Startup SQL still runs because `spring.sql.init.mode=always`, but it is safe for an existing database:

- `schema.sql` uses `CREATE TABLE IF NOT EXISTS`
- `data.sql` uses `ON CONFLICT DO NOTHING`

That means startup will:

- create tables only if missing
- insert sample data only if the rows do not already exist

### Seed Data

The sample seed currently contains:

- 10 departments
- 300 employees
- 300 employee-to-department assignments

## 🔐 Environment Variables

The app reads secrets from environment variables:

- `OPENAI_API_KEY`
- `POSTGRES_PASSWORD`

Example `.env`:

```env
OPENAI_API_KEY=your_openai_key_here
POSTGRES_PASSWORD=your_postgres_password_here
```

## ▶️ Running The App

Start from the terminal:

```bash
export OPENAI_API_KEY=your_openai_key_here
export POSTGRES_PASSWORD=your_postgres_password_here
./gradlew bootRun
```

Or use the VS Code task:

- `Gradle: bootRun`

Your `.vscode/tasks.json` is configured to source `${workspaceFolder}/.env` automatically before running `bootRun`.

## 🐞 Debugging

VS Code debug options are configured in `.vscode/launch.json`:

- `Debug Spring Boot (Gradle bootRun)`
- `Launch Spring Boot Main`

Both are wired to work with `.env`.

## 📘 Swagger

Swagger UI:

- `http://localhost:8080/mcpservice/swagger-ui/index.html`

OpenAPI JSON:

- `http://localhost:8080/mcpservice/v3/api-docs`

Swagger can be used to test:

- `POST /api/chat`
- `POST /api/graphql`

CORS is configured to allow requests from:

- `http://localhost:5174`

and covers:

- `/api/**`
- `/graphql`
- `/swagger-ui/**`
- `/swagger-ui.html`
- `/v3/api-docs/**`

## 📋 Logging

The GraphQL-backed MCP tools log:

- the tool name
- the final GraphQL query text
- the variables sent with the query
- success or failure outcome

This helps trace exactly what the LLM ended up executing.

## ⚠️ Notes

- The primary UI entrypoint is `/api/chat`.
- The primary LLM tool layer is `CompanyMcpGraphQlTools`.
- The GraphQL REST bridge exists mainly for Swagger/manual testing.
- If `OPENAI_API_KEY` is missing or invalid, chat requests will fail.
- If PostgreSQL is unavailable, the app will fail on startup.

## 🚀 Useful Commands

Run the app:

```bash
./gradlew bootRun
```

Compile only:

```bash
./gradlew classes
```

Run tests:

```bash
./gradlew test
```
