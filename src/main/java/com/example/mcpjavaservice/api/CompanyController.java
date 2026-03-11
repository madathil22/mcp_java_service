package com.example.mcpjavaservice.api;

import com.example.mcpjavaservice.department.DepartmentResponse;
import com.example.mcpjavaservice.department.DepartmentService;
import com.example.mcpjavaservice.employee.EmployeeResponse;
import com.example.mcpjavaservice.employee.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Company", description = "Employee and department lookup endpoints")
public class CompanyController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public CompanyController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping("/getallemployees")
    @Operation(summary = "Get all employees", description = "Returns every employee with the departments they belong to.")
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/getalldepartments")
    @Operation(summary = "Get all departments", description = "Returns every department with the employees mapped to it.")
    public List<DepartmentResponse> getAllDepartments() {
        return departmentService.getAllDepartments();
    }
}
