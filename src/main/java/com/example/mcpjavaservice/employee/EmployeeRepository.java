package com.example.mcpjavaservice.employee;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Override
    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.department"})
    List<Employee> findAll();
}
