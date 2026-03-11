package com.example.mcpjavaservice.employee;

import com.example.mcpjavaservice.department.Department;
import com.example.mcpjavaservice.department.DepartmentNotFoundException;
import com.example.mcpjavaservice.department.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
            .stream()
            .sorted(Comparator.comparing(Employee::getEmployeeId))
            .map(this::toResponse)
            .toList();
    }

    public EmployeeResponse getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
            .map(this::toResponse)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
    }

    public List<EmployeeResponse> getEmployeesByDepartmentName(String departmentName) {
        Department department = departmentRepository.findByDepartmentNameIgnoreCase(departmentName)
            .orElseThrow(() -> new DepartmentNotFoundException("Department not found with name: " + departmentName));

        return employeeRepository.findAllByDepartmentNameIgnoreCase(department.getDepartmentName())
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public List<EmployeeResponse> getEmployeesByDepartmentId(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new DepartmentNotFoundException("Department not found with id: " + departmentId);
        }

        return employeeRepository.findAllByDepartmentId(departmentId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    private EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
            employee.getEmployeeId(),
            employee.getName(),
            employee.getAge(),
            employee.getGender(),
            employee.getEmployeeDepartments()
                .stream()
                .map(employeeDepartment -> employeeDepartment.getDepartment())
                .sorted(Comparator.comparing(Department::getDepartmentId))
                .map(department -> new EmployeeResponse.DepartmentSummary(
                    department.getDepartmentId(),
                    department.getDepartmentName()
                ))
                .toList()
        );
    }
}
