package com.example.mcpjavaservice.graphql.model;

import java.util.List;

public record GraphQlDepartment(
    Long departmentId,
    String departmentName,
    Integer employeeCount,
    List<GraphQlEmployeeSummary> employees
) {
}
