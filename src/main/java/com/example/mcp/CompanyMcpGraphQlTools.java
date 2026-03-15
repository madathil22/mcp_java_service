package com.example.mcp;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CompanyMcpGraphQlTools {

    private static final Logger logger = LoggerFactory.getLogger(CompanyMcpGraphQlTools.class);

    private static final String EMPLOYEES_QUERY = """
        query Employees($filter: EmployeeFilterInput) {
          employees(filter: $filter) {
            employeeId
            name
            age
            gender
            departments {
              departmentId
              departmentName
            }
          }
        }
        """;

    private static final String DEPARTMENTS_QUERY = """
        query Departments($filter: DepartmentFilterInput) {
          departments(filter: $filter) {
            departmentId
            departmentName
            employeeCount
            employees {
              employeeId
              name
              age
              gender
            }
          }
        }
        """;

    private static final String COUNT_BY_DEPARTMENT_QUERY = """
        query CountEmployeesByDepartment($filter: DepartmentFilterInput) {
          countEmployeesByDepartment(filter: $filter) {
            departmentId
            departmentName
            employeeCount
          }
        }
        """;

    private final GraphQlSource graphQlSource;

    public CompanyMcpGraphQlTools(GraphQlSource graphQlSource) {
        this.graphQlSource = graphQlSource;
    }

    @Tool(name = "graphqlEmployees", description = "Query employees with optional filters such as department name, gender, name fragment, minimum age, and maximum age. Use this for requests like employees in a department, female employees in Engineering, employees older than 40, or employees by age range.")
    public Object employees(
        @ToolParam(description = "Optional employee name fragment to match case-insensitively", required = false) String nameContains,
        @ToolParam(description = "Optional department name filter", required = false) String departmentName,
        @ToolParam(description = "Optional gender filter", required = false) String gender,
        @ToolParam(description = "Optional minimum age filter", required = false) Integer minAge,
        @ToolParam(description = "Optional maximum age filter", required = false) Integer maxAge
    ) {
        Map<String, Object> filter = new LinkedHashMap<>();
        putIfPresent(filter, "nameContains", nameContains);
        putIfPresent(filter, "departmentName", departmentName);
        putIfPresent(filter, "gender", gender);
        putIfPresent(filter, "minAge", minAge);
        putIfPresent(filter, "maxAge", maxAge);
        return execute("graphqlEmployees", EMPLOYEES_QUERY, Map.of("filter", filter));
    }

    @Tool(name = "graphqlDepartments", description = "Query departments with optional filters and include their employees. Use this for requests like departments with their employees, departments for employee John, or departments containing female employees in an age range.")
    public Object departments(
        @ToolParam(description = "Optional department name fragment", required = false) String departmentName,
        @ToolParam(description = "Optional employee name fragment used to filter matching departments", required = false) String employeeName,
        @ToolParam(description = "Optional employee gender filter within matching departments", required = false) String employeeGender,
        @ToolParam(description = "Optional minimum employee age filter within matching departments", required = false) Integer employeeMinAge,
        @ToolParam(description = "Optional maximum employee age filter within matching departments", required = false) Integer employeeMaxAge
    ) {
        Map<String, Object> filter = new LinkedHashMap<>();
        putIfPresent(filter, "departmentName", departmentName);
        putIfPresent(filter, "employeeName", employeeName);
        putIfPresent(filter, "employeeGender", employeeGender);
        putIfPresent(filter, "employeeMinAge", employeeMinAge);
        putIfPresent(filter, "employeeMaxAge", employeeMaxAge);
        return execute("graphqlDepartments", DEPARTMENTS_QUERY, Map.of("filter", filter));
    }

    @Tool(name = "graphqlCountEmployeesByDepartment", description = "Count employees by department with optional department or employee filters. Use this for requests like count employees by department or count female employees by department.")
    public Object countEmployeesByDepartment(
        @ToolParam(description = "Optional department name fragment", required = false) String departmentName,
        @ToolParam(description = "Optional employee name fragment used to filter counts", required = false) String employeeName,
        @ToolParam(description = "Optional employee gender filter used to filter counts", required = false) String employeeGender,
        @ToolParam(description = "Optional minimum employee age filter used to filter counts", required = false) Integer employeeMinAge,
        @ToolParam(description = "Optional maximum employee age filter used to filter counts", required = false) Integer employeeMaxAge
    ) {
        Map<String, Object> filter = new LinkedHashMap<>();
        putIfPresent(filter, "departmentName", departmentName);
        putIfPresent(filter, "employeeName", employeeName);
        putIfPresent(filter, "employeeGender", employeeGender);
        putIfPresent(filter, "employeeMinAge", employeeMinAge);
        putIfPresent(filter, "employeeMaxAge", employeeMaxAge);
        return execute("graphqlCountEmployeesByDepartment", COUNT_BY_DEPARTMENT_QUERY, Map.of("filter", filter));
    }

    private Object execute(String toolName, String query, Map<String, Object> variables) {
        Map<String, Object> sanitizedVariables = variables.entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .filter(entry -> !(entry.getValue() instanceof Map<?, ?> map && map.isEmpty()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (left, right) -> right, LinkedHashMap::new));

        logger.info("Executing GraphQL tool {} with query:\n{}", toolName, query.strip());
        logger.info("GraphQL tool {} variables: {}", toolName, sanitizedVariables);

        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
            .query(query)
            .variables(sanitizedVariables)
            .build();

        ExecutionResult executionResult = graphQlSource.graphQl().execute(executionInput);
        if (!executionResult.getErrors().isEmpty()) {
            String message = executionResult.getErrors().stream()
                .map(error -> error.getMessage())
                .collect(Collectors.joining("; "));
            logger.error("GraphQL tool {} failed: {}", toolName, message);
            throw new IllegalArgumentException("GraphQL tool execution failed: " + message);
        }

        logger.info("GraphQL tool {} completed successfully", toolName);
        return executionResult.getData();
    }

    private void putIfPresent(Map<String, Object> target, String key, Object value) {
        if (value instanceof String text) {
            if (!text.isBlank()) {
                target.put(key, text);
            }
            return;
        }
        if (value != null) {
            target.put(key, value);
        }
    }
}
