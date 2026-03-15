# Repository Instructions For Coding Agents

Read `INSTRUCTIONS.md` first before re-analyzing the project.

## What This Repo Is

- Spring Boot service for company employees and departments
- Java 21 / Gradle
- REST API, GraphQL API, and MCP server in the same process
- PostgreSQL-backed application with idempotent startup SQL
- Spring AI chat flow grounded by MCP tools that execute GraphQL queries

## Non-Negotiable Project Facts

- Context path is `/mcpservice`
- REST API lives under `/mcpservice/api`
- The main UI endpoint is `POST /mcpservice/api/chat`
- A Swagger-testable GraphQL bridge exists at `POST /mcpservice/api/graphql`
- Native GraphQL is exposed at `/mcpservice/graphql`
- GraphQL schema lives in `src/main/resources/graphql/schema.graphqls`
- GraphQL query mappings live in `src/main/java/com/example/mcpjavaservice/graphql/CompanyGraphQlController.java`
- The LLM-facing MCP tool layer is implemented in `src/main/java/com/example/mcp/CompanyMcpGraphQlTools.java`
- MCP transport is configured through Spring AI MCP Server WebMVC

## When Working On Chat Or Query Features

- Do not call an LLM directly from the browser
- Keep API keys and model config on the server side
- Prefer extending the GraphQL schema and service filtering rather than adding many REST endpoints
- For plain-English chat, route the model through `CompanyMcpGraphQlTools`
- Reuse `EmployeeService` and `DepartmentService` for business logic
- Keep `/api/chat` as the primary UI-facing conversational entrypoint
- Keep `/api/graphql` available for Swagger/manual testing of GraphQL queries

## Database Rules

- PostgreSQL is the active datastore
- `schema.sql` must stay idempotent with `CREATE TABLE IF NOT EXISTS`
- `data.sql` must stay idempotent with `ON CONFLICT DO NOTHING`
- Avoid reintroducing destructive schema bootstrap behavior

## Start Here

1. `INSTRUCTIONS.md`
2. `build.gradle`
3. `src/main/resources/application.yml`
4. `src/main/resources/graphql/schema.graphqls`
5. `src/main/java/com/example/mcpjavaservice/api/ChatService.java`
6. `src/main/java/com/example/mcp/CompanyMcpGraphQlTools.java`
7. `README.md`
