# Project Instructions

This repository is a Spring Boot 3.2 / Java 21 service that exposes both a REST API and an MCP server over WebMVC.

## Core Purpose

- Manage company department and employee data from an in-memory HSQL database.
- Expose that data through REST endpoints under `/mcpservice/api`.
- Expose the same domain operations as MCP tools from `src/main/java/com/example/mcp/CompanyMcpTools.java`.

## Current Runtime Configuration

- Port: `8080`
- Servlet context path: `/mcpservice`
- Swagger UI: `/mcpservice/swagger-ui.html`
- MCP server name: `mcp-java-service`
- MCP protocol setting: `STREAMABLE`

Configuration source: `src/main/resources/application.yml`

## Backend Stack

- Spring Boot `3.2.0`
- Java toolchain `21`
- Spring Web MVC
- Spring Data JPA
- HSQLDB in-memory database
- Springdoc OpenAPI
- Spring AI MCP Server WebMVC via `org.springframework.ai:spring-ai-starter-mcp-server-webmvc`

Build file: `build.gradle`

## Domain Structure

- REST/API package: `src/main/java/com/example/mcpjavaservice/api`
- Department domain: `src/main/java/com/example/mcpjavaservice/department`
- Employee domain: `src/main/java/com/example/mcpjavaservice/employee`
- MCP tool package: `src/main/java/com/example/mcp`

Application entrypoint:

- `src/main/java/com/example/mcpjavaservice/McpJavaServiceApplication.java`

## Existing MCP Tools

Defined in `src/main/java/com/example/mcp/CompanyMcpTools.java`:

- `listDepartments()`
- `getDepartmentById(Long id)`
- `getDepartmentByName(String name)`
- `listEmployees()`
- `getEmployeeById(Long id)`
- `getEmployeesByDepartment(String departmentName)`

Important behavior:

- Department-name matching is case-insensitive.
- MCP tools call the service layer directly; they do not call REST endpoints internally.
- `getEmployeesByDepartment` returns a `DepartmentEmployeesResult` record containing department, count, and employees.

## Existing REST Surface

Base path: `/mcpservice/api`

Representative endpoints:

- `GET /getallemployees`
- `GET /getalldepartments`
- `GET /departments/{id}`
- `GET /departments/name/{name}`
- `GET /departments/{id}/employees`
- `GET /employees/{id}`
- `GET /employees/by-department/{departmentName}`

## Data Model Notes

- Database is seeded on startup from SQL init scripts.
- Data is ephemeral because HSQLDB is in-memory.
- Schema centers on `employee`, `department`, and `employee_department`.

## Guidance For Future Work

- If building chat functionality, add a backend chat endpoint instead of calling an LLM directly from the browser.
- Reuse `CompanyMcpTools` for grounding answers rather than duplicating company-query logic.
- Keep secrets in environment variables or config placeholders, never in frontend code.
- Preserve the existing `/mcpservice` context path unless there is a deliberate migration.
- Prefer small vertical-slice changes over speculative refactors.

## Files Worth Reading First

- `build.gradle`
- `src/main/resources/application.yml`
- `src/main/java/com/example/mcp/CompanyMcpTools.java`
- `src/main/java/com/example/mcpjavaservice/api/CompanyController.java`
- `README.md`
