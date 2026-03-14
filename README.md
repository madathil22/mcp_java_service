# 🤖 MCP Java Service

A Spring Boot service that exposes a single chat API for a UI, uses Spring AI to interpret natural-language company queries, and delegates the actual work to MCP-style tools backed by a PostgreSQL database.

## ✨ What It Does

- Accepts user prompts like `give me all employees in engineering`
- Uses Spring AI + OpenAI to decide which backend tool to call
- Exposes MCP tools from `CompanyMcpTools` for employee and department queries
- Serves Swagger UI for quick inspection
- Connects to PostgreSQL instead of an in-memory database
- Initializes schema and seed data idempotently on startup

## 🧱 Stack

- Java 21
- Gradle
- Spring Boot 3.2
- Spring Web MVC
- Spring Data JPA
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
src/test/java
.vscode
.env
```

## 🔌 Main HTTP Endpoint

Base path:

```text
/mcpservice/api
```

Chat endpoint:

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

Example curl:

```bash
curl -X POST http://localhost:8080/mcpservice/api/chat \
  -H 'Content-Type: application/json' \
  -d '{"message":"Give me all employees for department Engineering"}'
```

## 🧠 How The Chat Flow Works

1. The UI sends a natural-language prompt to `/api/chat`.
2. `ChatService` builds a Spring AI `ChatClient` request.
3. Spring AI exposes `CompanyMcpTools` as callable tools.
4. The model decides which tool to invoke.
5. The tool uses the normal service/repository layer.
6. The backend returns the final assistant reply to the UI.

## 🛠️ MCP Tools Available

Defined in [src/main/java/com/example/mcp/CompanyMcpTools.java](/workspaces/mcpjavaservice/src/main/java/com/example/mcp/CompanyMcpTools.java):

- `listDepartments`
- `getDepartmentById`
- `getDepartmentByName`
- `listEmployees`
- `getEmployeeById`
- `getEmployeesByDepartment`

## 🗃️ Database

The service now uses PostgreSQL:

- Host: `host.docker.internal`
- Port: `5432`
- Database: `mcpdb`
- User: `mcpuser`

Configured in [src/main/resources/application.yml](/workspaces/mcpjavaservice/src/main/resources/application.yml).

### Schema Initialization

Startup SQL still runs because `spring.sql.init.mode=always`, but it is now safe for an existing database:

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

CORS is configured to allow requests from:

- `http://localhost:5174`

and covers:

- `/api/**`
- `/swagger-ui/**`
- `/swagger-ui.html`
- `/v3/api-docs/**`

## ⚠️ Notes

- The HTTP surface is intentionally small: the UI should call `/api/chat`.
- The MCP tools are still available inside the app as the backend capability layer.
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
