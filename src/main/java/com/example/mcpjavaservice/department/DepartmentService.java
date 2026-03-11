package com.example.mcpjavaservice.department;

import com.example.mcpjavaservice.employee.EmployeeResponse;
import com.example.mcpjavaservice.employee.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;

    public DepartmentService(DepartmentRepository departmentRepository, EmployeeService employeeService) {
        this.departmentRepository = departmentRepository;
        this.employeeService = employeeService;
    }

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
            .stream()
            .sorted(Comparator.comparing(Department::getDepartmentId))
            .map(this::toResponse)
            .toList();
    }

    public DepartmentResponse getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
            .map(this::toResponse)
            .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + departmentId));
    }

    public DepartmentResponse getDepartmentByName(String departmentName) {
        return departmentRepository.findByDepartmentNameIgnoreCase(departmentName)
            .map(this::toResponse)
            .orElseThrow(() -> new DepartmentNotFoundException("Department not found with name: " + departmentName));
    }

    public List<EmployeeResponse> getEmployeesByDepartmentId(Long departmentId) {
        return employeeService.getEmployeesByDepartmentId(departmentId);
    }

    private DepartmentResponse toResponse(Department department) {
        return new DepartmentResponse(
            department.getDepartmentId(),
            department.getDepartmentName()
        );
    }
}
