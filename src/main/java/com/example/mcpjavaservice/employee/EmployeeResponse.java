package com.example.mcpjavaservice.employee;

import java.util.List;

public record EmployeeResponse(
    Long employeeId,
    String name,
    Integer age,
    String gender,
    List<DepartmentSummary> departments
) {

    public record DepartmentSummary(Long departmentId, String departmentName) {
    }
}
