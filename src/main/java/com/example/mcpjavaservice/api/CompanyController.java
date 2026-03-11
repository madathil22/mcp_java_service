package com.example.mcpjavaservice.api;

import com.example.mcpjavaservice.department.DepartmentResponse;
import com.example.mcpjavaservice.department.DepartmentService;
import com.example.mcpjavaservice.employee.EmployeeResponse;
import com.example.mcpjavaservice.employee.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Operation(summary = "Get all departments", description = "Returns every department.")
    public List<DepartmentResponse> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/departments/{id}")
    @Operation(summary = "Get department by id")
    public DepartmentResponse getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @GetMapping("/departments/name/{name}")
    public DepartmentResponse getDepartmentByName(
        @Parameter(description = "Department name, case-insensitive")
        @PathVariable String name
    ) {
        return departmentService.getDepartmentByName(name);
    }

    @GetMapping("/departments/{id}/employees")
    @Operation(summary = "Get employees for a department")
    public List<EmployeeResponse> getEmployeesByDepartmentId(@PathVariable Long id) {
        return departmentService.getEmployeesByDepartmentId(id);
    }

    @GetMapping("/employees/{id}")
    @Operation(summary = "Get employee by id")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/employees/by-department/{departmentName}")
    public List<EmployeeResponse> getEmployeesByDepartmentName(@PathVariable String departmentName) {
        return employeeService.getEmployeesByDepartmentName(departmentName);
    }
}
