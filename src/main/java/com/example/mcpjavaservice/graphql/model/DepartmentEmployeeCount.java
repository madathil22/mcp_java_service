package com.example.mcpjavaservice.graphql.model;

public record DepartmentEmployeeCount(
    Long departmentId,
    String departmentName,
    Integer employeeCount
) {
}
