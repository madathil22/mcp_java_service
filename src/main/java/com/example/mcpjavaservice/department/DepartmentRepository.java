package com.example.mcpjavaservice.department;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Override
    List<Department> findAll();

    Optional<Department> findByDepartmentNameIgnoreCase(String departmentName);

    @Override
    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.employee"})
    Optional<Department> findById(Long id);
}
