package com.example.mcpjavaservice.employee;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
            .stream()
            .sorted(Comparator.comparing(Employee::getEmployeeId))
            .map(employee -> new EmployeeResponse(
                employee.getEmployeeId(),
                employee.getName(),
                employee.getAge(),
                employee.getGender(),
                employee.getEmployeeDepartments()
                    .stream()
                    .map(employeeDepartment -> employeeDepartment.getDepartment())
                    .sorted(Comparator.comparing(department -> department.getDepartmentId()))
                    .map(department -> new EmployeeResponse.DepartmentSummary(
                        department.getDepartmentId(),
                        department.getDepartmentName()
                    ))
                    .toList()
            ))
            .toList();
    }
}
