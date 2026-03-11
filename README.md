# MCP Java Service

Spring Boot 3.2 service with:

- a REST API for employees and departments
- Swagger UI for interactive API testing
- an in-memory HSQL database seeded at startup
- Spring AI MCP server support in the same application

## Stack

- Java 21
- Gradle
- Spring Boot 3.2.0
- Spring Web MVC
- Spring Data JPA
- HSQLDB
- Springdoc OpenAPI / Swagger UI
- Spring AI MCP Server (WebMVC)

## Running The App

Start the application:

```bash
./gradlew bootRun
```

Run tests:

```bash
./gradlew test
```

## Default Configuration

- Port: `8080`
- Context path: `/mcpservice`
- Swagger UI: `http://localhost:8080/mcpservice/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/mcpservice/v3/api-docs`

## Database Model

The in-memory HSQL database is initialized from `schema.sql` and `data.sql`.

Tables:

- `employee`
  Columns: `employee_id`, `name`, `age`, `gender`
- `department`
  Columns: `department_id`, `department_name`
- `employee_department`
  Columns: `employee_id`, `department_id`

## REST API

Base path:

```text
/mcpservice/api
```

### Existing list endpoints

- `GET /getallemployees`
- `GET /getalldepartments`

### Department endpoints

- `GET /departments/{id}`
- `GET /departments/name/{name}`
- `GET /departments/{id}/employees`

### Employee endpoints

- `GET /employees/{id}`
- `GET /employees/by-department/{departmentName}`

## Example Requests

```bash
curl http://localhost:8080/mcpservice/api/getallemployees
curl http://localhost:8080/mcpservice/api/getalldepartments
curl http://localhost:8080/mcpservice/api/departments/10
curl http://localhost:8080/mcpservice/api/departments/name/engineering
curl http://localhost:8080/mcpservice/api/departments/10/employees
curl http://localhost:8080/mcpservice/api/employees/2
curl http://localhost:8080/mcpservice/api/employees/by-department/engineering
```

## Error Responses

Not found cases return structured JSON like this:

```json
{
  "timestamp": "2026-03-11T03:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Department not found with id: 999",
  "path": "/mcpservice/api/departments/999"
}
```

## MCP Server

This application also exposes MCP tools from [CompanyMcpTools.java](/workspaces/mcpjavaservice/src/main/java/com/example/mcp/CompanyMcpTools.java).

Available tools:

- `listDepartments`
- `getDepartmentById`
- `getDepartmentByName`
- `listEmployees`
- `getEmployeeById`
- `getEmployeesByDepartment`

The MCP tool layer calls the same service layer as the REST API and does not call HTTP endpoints internally.

## Project Layout

```text
src/main/java/com/example/mcpjavaservice
src/main/java/com/example/mcp
src/main/resources
src/test/java
```

## Notes

- Department-name lookup is case-insensitive in both REST and MCP flows.
- Swagger and MCP server support coexist in the same application.
- The database is in-memory, so data resets on restart.
