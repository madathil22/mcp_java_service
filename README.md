# mcp_java_service
backend java service

Spring Boot service backed by an in-memory HSQL database.

Run locally with:

```bash
gradle bootRun
```

Test with:

```bash
gradle test
```

Default server settings:

- Port: `9090`
- Context path: `/mcpservice`

Endpoints:

- `GET /mcpservice/api/getallemployees` returns employees with their linked departments.
- `GET /mcpservice/api/getalldepartments` returns departments with their linked employees.
- Swagger UI is available at `/mcpservice/swagger-ui.html`.
