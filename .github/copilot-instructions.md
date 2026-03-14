# Repository Instructions For Coding Agents

Read `INSTRUCTIONS.md` first before re-analyzing the project.

## What This Repo Is

- Spring Boot service for company employees and departments
- Java 21 / Gradle
- REST API and MCP server in the same process
- In-memory HSQLDB seeded at startup

## Non-Negotiable Project Facts

- Context path is `/mcpservice`
- REST API lives under `/mcpservice/api`
- MCP tools are implemented in `src/main/java/com/example/mcp/CompanyMcpTools.java`
- MCP transport is configured through Spring AI MCP Server WebMVC
- Existing MCP tools already cover department and employee lookup needs

## When Adding A Chat UI

- Do not call an LLM directly from the browser
- Add a backend chat endpoint in Spring Boot
- Use the existing MCP tool layer to ground answers
- Keep API keys and model config on the server side
- Prefer incremental changes that fit the current stack

## Start Here

1. `INSTRUCTIONS.md`
2. `build.gradle`
3. `src/main/resources/application.yml`
4. `src/main/java/com/example/mcp/CompanyMcpTools.java`
5. `README.md`
