package com.example.mcpjavaservice.graphql.input;

public record DepartmentFilterInput(
    String departmentName,
    String employeeName,
    String employeeGender,
    Integer employeeMinAge,
    Integer employeeMaxAge
) {
}
