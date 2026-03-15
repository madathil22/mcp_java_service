package com.example.mcpjavaservice.graphql;

import com.example.mcpjavaservice.department.DepartmentService;
import com.example.mcpjavaservice.employee.EmployeeService;
import com.example.mcpjavaservice.graphql.input.DepartmentFilterInput;
import com.example.mcpjavaservice.graphql.input.EmployeeFilterInput;
import com.example.mcpjavaservice.graphql.model.DepartmentEmployeeCount;
import com.example.mcpjavaservice.graphql.model.GraphQlDepartment;
import com.example.mcpjavaservice.graphql.model.GraphQlEmployee;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CompanyGraphQlController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public CompanyGraphQlController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @QueryMapping
    public List<GraphQlEmployee> employees(@Argument EmployeeFilterInput filter) {
        return employeeService.findEmployees(filter);
    }

    @QueryMapping
    public List<GraphQlDepartment> departments(@Argument DepartmentFilterInput filter) {
        return departmentService.findDepartments(filter);
    }

    @QueryMapping
    public List<DepartmentEmployeeCount> countEmployeesByDepartment(@Argument DepartmentFilterInput filter) {
        return departmentService.countEmployeesByDepartment(filter);
    }
}
