package com.example.mcpjavaservice.graphql.model;

import java.util.List;

public record GraphQlEmployee(
    Long employeeId,
    String name,
    Integer age,
    String gender,
    List<GraphQlDepartmentSummary> departments
) {
}
