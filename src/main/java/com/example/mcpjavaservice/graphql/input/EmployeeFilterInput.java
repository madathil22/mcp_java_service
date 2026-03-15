package com.example.mcpjavaservice.graphql.input;

public record EmployeeFilterInput(
    String nameContains,
    String departmentName,
    String gender,
    Integer minAge,
    Integer maxAge
) {
}
