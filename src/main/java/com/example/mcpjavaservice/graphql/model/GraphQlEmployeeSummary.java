package com.example.mcpjavaservice.graphql.model;

public record GraphQlEmployeeSummary(
    Long employeeId,
    String name,
    Integer age,
    String gender
) {
}
