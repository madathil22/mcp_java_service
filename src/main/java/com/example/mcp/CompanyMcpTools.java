package com.example.mcp;

import com.example.mcpjavaservice.department.DepartmentResponse;
import com.example.mcpjavaservice.department.DepartmentService;
import com.example.mcpjavaservice.employee.EmployeeResponse;
import com.example.mcpjavaservice.employee.EmployeeService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMcpTools {

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    public CompanyMcpTools(DepartmentService departmentService, EmployeeService employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @Tool(name = "listDepartments", description = "List all departments.")
    public List<DepartmentResponse> listDepartments() {
        return departmentService.getAllDepartments();
    }

    @Tool(name = "getDepartmentById", description = "Get a single department by numeric id.")
    public DepartmentResponse getDepartmentById(
        @ToolParam(description = "Department id", required = true) Long id
    ) {
        return departmentService.getDepartmentById(id);
    }

    @Tool(name = "getDepartmentByName", description = "Get a single department by name using case-insensitive matching.")
    public DepartmentResponse getDepartmentByName(
        @ToolParam(description = "Department name", required = true) String name
    ) {
        return departmentService.getDepartmentByName(name);
    }

    @Tool(name = "listEmployees", description = "List all employees with their departments.")
    public List<EmployeeResponse> listEmployees() {
        return employeeService.getAllEmployees();
    }

    @Tool(name = "getEmployeeById", description = "Get a single employee by numeric id.")
    public EmployeeResponse getEmployeeById(
        @ToolParam(description = "Employee id", required = true) Long id
    ) {
        return employeeService.getEmployeeById(id);
    }

    @Tool(name = "getEmployeesByDepartment", description = "Get a department plus the employees assigned to it. Department name matching is case-insensitive.")
    public DepartmentEmployeesResult getEmployeesByDepartment(
        @ToolParam(description = "Department name", required = true) String departmentName
    ) {
        DepartmentResponse department = departmentService.getDepartmentByName(departmentName);
        List<EmployeeResponse> employees = employeeService.getEmployeesByDepartmentName(departmentName);
        return new DepartmentEmployeesResult(department, employees.size(), employees);
    }

    public record DepartmentEmployeesResult(
        DepartmentResponse department,
        int employeeCount,
        List<EmployeeResponse> employees
    ) {
    }
}
