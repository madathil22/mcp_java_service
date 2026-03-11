package com.example.mcpjavaservice.department;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
            .stream()
            .sorted(Comparator.comparing(Department::getDepartmentId))
            .map(department -> new DepartmentResponse(
                department.getDepartmentId(),
                department.getDepartmentName()
            ))
            .toList();
    }
}
